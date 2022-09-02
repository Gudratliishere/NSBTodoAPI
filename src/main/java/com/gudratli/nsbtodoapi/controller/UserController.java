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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api("User controller")
public class UserController
{
    private final UserService userService;
    private final FileService fileService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Get All", notes = "Returns all users.")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getAll ()
    {
        List<User> users = userService.getAll();

        return ResponseEntity.ok(getResponseWithList(users));
    }

    @GetMapping("/getByNameContaining/{name}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Get by name containing", notes = "Returns list of users contains that name key.")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getByNameContaining (@PathVariable @ApiParam(name = "Name",
            value = "Name of the user", required = true, example = "Dun") String name)
    {
        List<User> users = userService.getByNameContaining(name);

        return ResponseEntity.ok(getResponseWithList(users));
    }

    @GetMapping("/getBySurnameContaining/{surname}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Get by surname containing", notes = "Returns list of users contains that surname key.")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getBySurnameContaining (@PathVariable @ApiParam(name = "Surname",
            value = "Surname of user", required = true, example = "Gud") String surname)
    {
        List<User> users = userService.getBySurnameContaining(surname);

        return ResponseEntity.ok(getResponseWithList(users));
    }

    @GetMapping("/getByCountryId/{countryId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Get by country ID", notes = "Returns list of users live in that country")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getByCountryId (@PathVariable @ApiParam(name = "ID",
            value = "ID of the country", required = true, example = "3") Integer countryId)
    {
        List<User> users = userService.getByCountryId(countryId);

        return ResponseEntity.ok(getResponseWithList(users));
    }

    @GetMapping("/getByRoleId/{roleId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Get by role ID", notes = "Returns list of users have that role")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getByRoleId (@PathVariable @ApiParam(name = "ID",
            value = "ID of the role", required = true, example = "1") Integer roleId)
    {
        List<User> users = userService.getByRoleId(roleId);

        return ResponseEntity.ok(getResponseWithList(users));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Get by ID", notes = "Returns single user according to it's ID.")
    public ResponseEntity<ResponseDTO<UserDTO>> getById (@PathVariable @ApiParam(name = "ID",
            value = "ID of the user", required = true, example = "9") Integer id)
    {
        User user = userService.getById(id);

        return ResponseEntity.ok(getResponse(user, "id."));
    }

    @GetMapping("/getByPhone/{phone}")
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Get by phone",
            notes = "Returns single user according to it's phone number, because phone number is unique.")
    public ResponseEntity<ResponseDTO<UserDTO>> getByPhone (@PathVariable @ApiParam(name = "Phone",
            value = "Phone number of user", required = true, example = "0222222222") String phone)
    {
        User user = userService.getByPhone(phone);

        return ResponseEntity.ok(getResponse(user, "phone."));
    }

    @GetMapping("/getByEmail/{email}")
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Get by email",
            notes = "Returns single user according to it's email, because email is unique.")
    public ResponseEntity<ResponseDTO<UserDTO>> getByEmail (@PathVariable @ApiParam(name = "Email",
            value = "Email address of user", required = true, example = "dunay@gmail.com") String email)
    {
        User user = userService.getByEmail(email);

        return ResponseEntity.ok(getResponse(user, "email."));
    }

    @GetMapping("/getByUsername/{username}")
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "GEt by username",
            notes = "Returns single user according to it's username, because username is unique.")
    public ResponseEntity<ResponseDTO<UserDTO>> getByUsername (@PathVariable @ApiParam(name = "Username",
            value = "Username of user", required = true, example = "dunayqudrtli") String username)
    {
        User user = userService.getByUsername(username);

        return ResponseEntity.ok(getResponse(user, "username."));
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Update", notes = "Update existing user.")
    public ResponseEntity<ResponseDTO<UserDTO>> update (@Valid @RequestBody @ApiParam(name = "User",
            value = "DTO for user", required = true) UserDTO userDTO)
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
    @ApiOperation(value = "Upload CV", notes = "Upload CV of user and get it's download URL.")
    public ResponseEntity<ResponseDTO<FileDTO>> uploadCV (@RequestParam("cv") @ApiParam(name = "cv",
            value = "CV file of user", required = true) MultipartFile multipartFile,
            @PathVariable @ApiParam(name = "ID", value = "ID of user", required = true, example = "16") Integer id)
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
    @ApiOperation(value = "Download CV", notes = "Download CV of user with it's ID.")
    public ResponseEntity<Resource> downloadCV (@PathVariable @ApiParam(name = "ID", value = "ID of user",
            required = true, example = "29") Integer id)
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
    @ApiOperation(value = "Delete", notes = "Delete single user with ID.")
    public ResponseEntity<ResponseDTO<UserDTO>> delete (@PathVariable @ApiParam(name = "ID", value = "ID of user",
            required = true, example = "31") Integer id)
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