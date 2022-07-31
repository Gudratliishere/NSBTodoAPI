package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.dto.RoleDTO;
import com.gudratli.nsbtodoapi.entity.Role;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateRoleException;
import com.gudratli.nsbtodoapi.service.inter.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
public class RoleController
{
    private final RoleService roleService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    public ResponseEntity<ResponseDTO<List<RoleDTO>>> getAll ()
    {
        List<Role> roleList = roleService.getAll();
        List<RoleDTO> roleDTOs = new ArrayList<>();

        roleList.forEach(role -> roleDTOs.add(converter.toRoleDTO(role)));

        ResponseDTO<List<RoleDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.successfullyFetched(roleDTOs);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    public ResponseEntity<ResponseDTO<RoleDTO>> getById (@PathVariable Integer id)
    {
        Role role = roleService.getById(id);

        return ResponseEntity.ok(getResponse(role, "id"));
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<ResponseDTO<RoleDTO>> getByName (@PathVariable String name)
    {
        Role role = roleService.getByName(name);

        return ResponseEntity.ok(getResponse(role, "name"));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<RoleDTO>> add (@RequestBody RoleDTO roleDTO)
    {
        Role role = converter.toRole(roleDTO);
        role.setId(null);

        ResponseDTO<RoleDTO> responseDTO = new ResponseDTO<>(roleDTO);

        try
        {
            role = roleService.add(role);
            responseDTO.successfullyInserted(converter.toRoleDTO(role));
        } catch (DuplicateRoleException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<RoleDTO>> update (@RequestBody RoleDTO roleDTO)
    {
        Role role = roleService.getById(roleDTO.getId());
        ResponseDTO<RoleDTO> responseDTO = new ResponseDTO<>(roleDTO);

        if (role == null)
            responseDTO.notFound("role", "id.");

        converter.toRole(role, roleDTO);

        try
        {
            role = roleService.update(role);
            responseDTO.successfullyUpdated(converter.toRoleDTO(role));
        } catch (DuplicateRoleException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<RoleDTO>> delete (@PathVariable Integer id)
    {
        Role role = roleService.getById(id);
        ResponseDTO<RoleDTO> responseDTO = new ResponseDTO<>();

        if (role == null)
            responseDTO.notFound("role", "id.");
        else
        {
            roleService.remove(id);
            responseDTO.successfullyDeleted(converter.toRoleDTO(role));
        }

        return ResponseEntity.ok(responseDTO);
    }

    private ResponseDTO<RoleDTO> getResponse (Role role, String parameter)
    {
        ResponseDTO<RoleDTO> responseDTO = new ResponseDTO<>();
        if (role == null)
            responseDTO.notFound("role", parameter);
        else
            responseDTO.successfullyFetched(converter.toRoleDTO(role));

        return responseDTO;
    }
}