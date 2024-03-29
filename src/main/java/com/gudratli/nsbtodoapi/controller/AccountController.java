package com.gudratli.nsbtodoapi.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudratli.nsbtodoapi.dto.*;
import com.gudratli.nsbtodoapi.entity.Role;
import com.gudratli.nsbtodoapi.entity.User;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateException;
import com.gudratli.nsbtodoapi.service.inter.RoleService;
import com.gudratli.nsbtodoapi.service.inter.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@Api("Account controller")
public class AccountController
{
    private final UserService userService;
    private final RoleService roleService;
    private final Converter converter;

    @GetMapping("/token/refresh")
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Refresh token", notes = "Send refresh token to get new token")
    public void refreshToken (HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String prefix = "Bearer ";
        if (authorizationHeader != null && authorizationHeader.startsWith(prefix))
        {
            try
            {
                String refreshToken = authorizationHeader.substring(prefix.length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                User user = userService.getByUsername(username);
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",
                                Collections.singletonList(user.getRole().getName()))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception ex)
            {
                ex.printStackTrace();
                response.setHeader("error", "Error");
                response.setStatus(HttpStatus.FORBIDDEN.value());
                List<String> error = Collections.singletonList(ex.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else
            throw new RuntimeException("Refresh token is missing.");
    }

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Register", notes = "Send user information to register account.")
    public ResponseEntity<ResponseDTO<UserDTO>> register (@Valid @RequestBody @ApiParam(name = "User Create DTO",
            value = "Send this DTO to add new account.", required = true) UserCreateDTO userCreateDTO)
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
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Change password", notes = "Send UserAuthDTO to change password.")
    public ResponseEntity<ResponseDTO<UserDTO>> changePassword (@Valid @RequestBody @ApiParam(name = "User Auth DTO",
            value = "Send this DTO to change password.", required = true) UserAuthDTO userAuthDTO)
    {
        User user = userService.getById(userAuthDTO.getId());
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        if (user == null)
            return ResponseEntity.ok(responseDTO.notFound("user", "id."));

        user = userService.changePassword(userAuthDTO.getId(), userAuthDTO.getPassword());

        return ResponseEntity.ok(responseDTO.successfullyUpdated(converter.toUserDTO(user)));
    }

    @GetMapping("/activate/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Activate user", notes = "Activate user status, so user can login to account.")
    public ResponseEntity<ResponseDTO<UserDTO>> activateUser (@PathVariable @ApiParam(name = "ID",
            value = "ID of user whom being activated.", required = true, example = "16") Integer id)
    {
        User user = userService.getById(id);
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        if (user == null)
            return ResponseEntity.ok(responseDTO.notFound("user", "id."));

        user = userService.changeStatus(id, true);

        return ResponseEntity.ok(responseDTO.successfullyUpdated(converter.toUserDTO(user)));
    }

    @GetMapping("/deactivate/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Deactivate user", notes = "Deactivate user status, so user can not login and use account.")
    public ResponseEntity<ResponseDTO<UserDTO>> deactivateUser (@PathVariable @ApiParam(name = "ID",
            value = "ID of user whom being deactivated.", required = true, example = "12") Integer id)
    {
        User user = userService.getById(id);
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        if (user == null)
            return ResponseEntity.ok(responseDTO.notFound("user", "id."));

        user = userService.changeStatus(id, false);

        return ResponseEntity.ok(responseDTO.successfullyUpdated(converter.toUserDTO(user)));
    }

    @GetMapping("/ban/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Ban user", notes = "Ban user, so user can not use account.")
    public ResponseEntity<ResponseDTO<UserDTO>> banUser (@PathVariable @ApiParam(name = "ID",
            value = "ID of user whom being banned.", required = true, example = "18") Integer id)
    {
        User user = userService.getById(id);
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        if (user == null)
            return ResponseEntity.ok(responseDTO.notFound("user", "id."));

        user = userService.changeBanned(id, true);

        return ResponseEntity.ok(responseDTO.successfullyUpdated(converter.toUserDTO(user)));
    }

    @GetMapping("/removeBan/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Remove ban", notes = "Remove ban from user, so user can user account again.")
    public ResponseEntity<ResponseDTO<UserDTO>> removeBanUser (@PathVariable @ApiParam(name = "ID",
            value = "ID of user whom removing ban from.", required = true, example = "19") Integer id)
    {
        User user = userService.getById(id);
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        if (user == null)
            return ResponseEntity.ok(responseDTO.notFound("user", "id."));

        user = userService.changeBanned(id, false);

        return ResponseEntity.ok(responseDTO.successfullyUpdated(converter.toUserDTO(user)));
    }

    @GetMapping("/changeRole/{id}/{roleId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Change role", notes = "Change role of user.")
    public ResponseEntity<ResponseDTO<UserDTO>> changeRole (@PathVariable @ApiParam(name = "ID",
            value = "ID of user whom role will be changed.", required = true, example = "15") Integer id, @PathVariable
    @ApiParam(name = "Role ID", value = "ID of role that will assign to user.", required = true, example = "2") Integer roleId)
    {
        User user = userService.getById(id);
        Role role = roleService.getById(roleId);
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        if (user == null)
            return ResponseEntity.ok(responseDTO.notFound("user", "id."));
        if (role == null)
            return ResponseEntity.ok(responseDTO.notFound("role", "id."));

        user = userService.changeRole(id, roleId);

        return ResponseEntity.ok(responseDTO.successfullyUpdated(converter.toUserDTO(user)));
    }
}