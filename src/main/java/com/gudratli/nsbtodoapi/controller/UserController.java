package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.FileDTO;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.dto.UserDTO;
import com.gudratli.nsbtodoapi.entity.User;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateException;
import com.gudratli.nsbtodoapi.service.inter.FileService;
import com.gudratli.nsbtodoapi.service.inter.UserService;
import com.gudratli.nsbtodoapi.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController
{
    private final UserService userService;
    private final FileService fileService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getAll ()
    {
        List<User> users = userService.getAll();

        return ResponseEntity.ok(getResponseWithList(users));
    }

    @GetMapping("/getByNameContaining/{name}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getByNameContaining (@PathVariable String name)
    {
        List<User> users = userService.getByNameContaining(name);

        return ResponseEntity.ok(getResponseWithList(users));
    }

    @GetMapping("/getBySurnameContaining/{surname}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getBySurnameContaining (@PathVariable String surname)
    {
        List<User> users = userService.getBySurnameContaining(surname);

        return ResponseEntity.ok(getResponseWithList(users));
    }

    @GetMapping("/getByCountryId/{countryId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getByCountryId (@PathVariable Integer countryId)
    {
        List<User> users = userService.getByCountryId(countryId);

        return ResponseEntity.ok(getResponseWithList(users));
    }

    @GetMapping("/getByRoleId/{roleId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getByRoleId (@PathVariable Integer roleId)
    {
        List<User> users = userService.getByRoleId(roleId);

        return ResponseEntity.ok(getResponseWithList(users));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<ResponseDTO<UserDTO>> getById (@PathVariable Integer id)
    {
        User user = userService.getById(id);

        return ResponseEntity.ok(getResponse(user, "id."));
    }

    @GetMapping("/getByPhone/{phone}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ResponseDTO<UserDTO>> getByPhone (@PathVariable String phone)
    {
        User user = userService.getByPhone(phone);

        return ResponseEntity.ok(getResponse(user, "phone."));
    }

    @GetMapping("/getByEmail/{email}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ResponseDTO<UserDTO>> getByEmail (@PathVariable String email)
    {
        User user = userService.getByEmail(email);

        return ResponseEntity.ok(getResponse(user, "email."));
    }

    @GetMapping("/getByUsername/{username}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ResponseDTO<UserDTO>> getByUsername (@PathVariable String username)
    {
        User user = userService.getByUsername(username);

        return ResponseEntity.ok(getResponse(user, "username."));
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<ResponseDTO<UserDTO>> update (@Valid @RequestBody UserDTO userDTO)
    {
        User user = userService.getById(userDTO.getId());
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>(userDTO);

        if (user == null)
            return ResponseEntity.ok(responseDTO.notFound("user", "id."));

        try
        {
            converter.toUser(user, userDTO);

            user = userService.update(user);
            responseDTO.successfullyUpdated(converter.toUserDTO(user));
        } catch (DuplicateException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/uploadCV/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<ResponseDTO<FileDTO>> uploadCV (@RequestParam("file") MultipartFile multipartFile,
            @PathVariable Integer id)
    {
        User user = userService.getById(id);
        ResponseDTO<FileDTO> responseDTO = new ResponseDTO<>();

        if (user == null)
            return ResponseEntity.ok(responseDTO.notFound("user", "id."));

        if (multipartFile != null && multipartFile.getOriginalFilename() != null)
        {
            String fileName = "CV" + id + '.' +
                    StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());

            com.gudratli.nsbtodoapi.entity.File file = new com.gudratli.nsbtodoapi.entity.File(fileName,
                    multipartFile.getContentType(), multipartFile.getSize());
            FileDTO fileDTO = converter.toFileDTO(file);
            fileDTO.setDownloadURL("http://localhost:8080/user/downloadCV/" + id);

            responseDTO.setObject(fileDTO);

            try
            {
                file = fileService.add(file);
                user.setCv(file);
                userService.update(user);

                responseDTO.successfullyUpdated(fileDTO);

                FileUtil.saveFile(FileUtil.CV_FILE_DIRECTORY, fileName, multipartFile);
            } catch (DuplicateException e)
            {
                responseDTO.duplicateException(e.getMessage());
            } catch (IOException e)
            {
                responseDTO.setErrorCode(500);
                responseDTO.setErrorMessage(e.getMessage());
            }
        }

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/downloadCV/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Resource> downloadCV (@PathVariable Integer id)
    {
        User user = userService.getById(id);
        if (user == null)
            return ResponseEntity.ok(null);

        File file = new File(FileUtil.CV_FILE_DIRECTORY + user.getCv().getName());

        try
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] arr = new byte[(int) file.length()];
            fileInputStream.read(arr);
            fileInputStream.close();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(user.getCv().getType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + user.getCv().getName() + "\"")
                    .body(new ByteArrayResource(arr));
        } catch (IOException e)
        {
            return ResponseEntity.ok(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseDTO<UserDTO>> delete (@PathVariable Integer id)
    {
        User user = userService.getById(id);
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        if (user == null)
            return ResponseEntity.ok(responseDTO.notFound("user", "id."));

        userService.remove(id);

        return ResponseEntity.ok(responseDTO.successfullyDeleted(converter.toUserDTO(user)));
    }

    private ResponseDTO<List<UserDTO>> getResponseWithList (List<User> users)
    {
        List<UserDTO> userDTOs = new ArrayList<>();

        users.forEach(user -> userDTOs.add(converter.toUserDTO(user)));

        ResponseDTO<List<UserDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.successfullyFetched(userDTOs);

        return responseDTO;
    }

    private ResponseDTO<UserDTO> getResponse (User user, String parameter)
    {
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        if (user == null)
            return responseDTO.notFound("user", parameter);

        UserDTO userDTO = converter.toUserDTO(user);

        return responseDTO.successfullyFetched(userDTO);
    }
}