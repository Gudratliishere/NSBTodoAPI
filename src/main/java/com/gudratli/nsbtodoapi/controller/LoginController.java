package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.*;
import com.gudratli.nsbtodoapi.entity.User;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateException;
import com.gudratli.nsbtodoapi.service.inter.RoleService;
import com.gudratli.nsbtodoapi.service.inter.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController
{
    private final UserService userService;
    private final RoleService roleService;
    private final Converter converter;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<UserDTO>> register (@RequestBody UserCreateDTO userCreateDTO)
    {
        User user = converter.toUser(userCreateDTO);
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>(converter.toUserDTO(user));

        try
        {
            user = userService.add(user);
            responseDTO.successfullyInserted(converter.toUserDTO(user));
        } catch (DuplicateException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<ResponseDTO<UserDTO>> changePassword (@RequestBody UserAuthDTO userAuthDTO)
    {
        User user = userService.getById(userAuthDTO.getId());
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        if (user == null)
            return ResponseEntity.ok(responseDTO.notFound("user", "id."));

        user = userService.changePassword(userAuthDTO.getId(), userAuthDTO.getPassword());

        return ResponseEntity.ok(responseDTO.successfullyUpdated(converter.toUserDTO(user)));
    }

    @GetMapping("/activate/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> activateUser (@PathVariable Integer id)
    {
        User user = userService.getById(id);
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        if (user == null)
            return ResponseEntity.ok(responseDTO.notFound("user", "id."));

        user = userService.changeStatus(id, true);

        return ResponseEntity.ok(responseDTO.successfullyUpdated(converter.toUserDTO(user)));
    }

    @GetMapping("/deactivate/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> deactivateUser (@PathVariable Integer id)
    {
        User user = userService.getById(id);
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        if (user == null)
            return ResponseEntity.ok(responseDTO.notFound("user", "id."));

        user = userService.changeStatus(id, false);

        return ResponseEntity.ok(responseDTO.successfullyUpdated(converter.toUserDTO(user)));
    }

    @GetMapping("/ban/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> banUser (@PathVariable Integer id)
    {
        User user = userService.getById(id);
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        if (user == null)
            return ResponseEntity.ok(responseDTO.notFound("user", "id."));

        user = userService.changeBanned(id, true);

        return ResponseEntity.ok(responseDTO.successfullyUpdated(converter.toUserDTO(user)));
    }

    @GetMapping("/removeBan/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> removeBanUser (@PathVariable Integer id)
    {
        User user = userService.getById(id);
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        if (user == null)
            return ResponseEntity.ok(responseDTO.notFound("user", "id."));

        user = userService.changeBanned(id, false);

        return ResponseEntity.ok(responseDTO.successfullyUpdated(converter.toUserDTO(user)));
    }

    @GetMapping("/changeRole/{id}/{roleId}")
    public ResponseEntity<ResponseDTO<UserDTO>> removeBanUser (@PathVariable Integer id, @PathVariable Integer roleId)
    {
        User user = userService.getById(id);
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        if (user == null)
            return ResponseEntity.ok(responseDTO.notFound("user", "id."));

        user = userService.changeRole(id, roleId);

        return ResponseEntity.ok(responseDTO.successfullyUpdated(converter.toUserDTO(user)));
    }
}