package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.dto.UserDTO;
import com.gudratli.nsbtodoapi.entity.User;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateException;
import com.gudratli.nsbtodoapi.service.inter.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController
{
    private final UserService userService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getAll ()
    {
        List<User> users = userService.getAll();

        return ResponseEntity.ok(getResponseWithList(users));
    }

    @GetMapping("/getByNameContaining/{name}")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getByNameContaining (@PathVariable String name)
    {
        List<User> users = userService.getByNameContaining(name);

        return ResponseEntity.ok(getResponseWithList(users));
    }

    @GetMapping("/getBySurnameContaining/{surname}")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getBySurnameContaining (@PathVariable String surname)
    {
        List<User> users = userService.getBySurnameContaining(surname);

        return ResponseEntity.ok(getResponseWithList(users));
    }

    @GetMapping("/getByCountryId/{countryId}")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getByCountryId (@PathVariable Integer countryId)
    {
        List<User> users = userService.getByCountryId(countryId);

        return ResponseEntity.ok(getResponseWithList(users));
    }

    @GetMapping("/getByRoleId/{roleId}")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getByRoleId (@PathVariable Integer roleId)
    {
        List<User> users = userService.getByRoleId(roleId);

        return ResponseEntity.ok(getResponseWithList(users));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    public ResponseEntity<ResponseDTO<UserDTO>> getById (@PathVariable Integer id)
    {
        User user = userService.getById(id);

        return ResponseEntity.ok(getResponse(user, "id."));
    }

    @GetMapping("/getByPhone/{phone}")
    public ResponseEntity<ResponseDTO<UserDTO>> getByPhone (@PathVariable String phone)
    {
        User user = userService.getByPhone(phone);

        return ResponseEntity.ok(getResponse(user, "phone."));
    }

    @GetMapping("/getByEmail/{email}")
    public ResponseEntity<ResponseDTO<UserDTO>> getByEmail (@PathVariable String email)
    {
        User user = userService.getByEmail(email);

        return ResponseEntity.ok(getResponse(user, "email."));
    }

    @GetMapping("/getByUsername/{username}")
    public ResponseEntity<ResponseDTO<UserDTO>> getByUsername (@PathVariable String username)
    {
        User user = userService.getByUsername(username);

        return ResponseEntity.ok(getResponse(user, "username."));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<UserDTO>> update (@RequestBody UserDTO userDTO)
    {
        User user = userService.getById(userDTO.getId());
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>(userDTO);

        if (user == null)
            return ResponseEntity.ok(responseDTO.notFound("user", "id."));

        converter.toUser(user, userDTO);

        try
        {
            user = userService.update(user);
            responseDTO.successfullyUpdated(converter.toUserDTO(user));
        } catch (DuplicateException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
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