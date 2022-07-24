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

        ResponseDTO<List<RoleDTO>> responseDTO = new ResponseDTO<>(roleDTOs);
        responseDTO.setSuccessMessage("Successfully fetched all data.");

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
            responseDTO.setObject(converter.toRoleDTO(role));
            responseDTO.setSuccessMessage("Successfully inserted new data.");
        } catch (DuplicateRoleException e)
        {
            responseDTO.setErrorCode(304);
            responseDTO.setErrorMessage(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<RoleDTO>> update (@RequestBody RoleDTO roleDTO)
    {
        Role role = roleService.getById(roleDTO.getId());
        ResponseDTO<RoleDTO> responseDTO = new ResponseDTO<>(roleDTO);

        if (role == null)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("There is not role with this id.");
            return ResponseEntity.ok(responseDTO);
        }

        converter.toRole(role, roleDTO);

        try
        {
            role = roleService.update(role);
            responseDTO.setObject(converter.toRoleDTO(role));
            responseDTO.setSuccessMessage("Successfully updated.");
        } catch (DuplicateRoleException e)
        {
            responseDTO.setErrorCode(304);
            responseDTO.setErrorMessage(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<RoleDTO>> delete (@PathVariable Integer id)
    {
        Role role = roleService.getById(id);
        ResponseDTO<RoleDTO> responseDTO = new ResponseDTO<>();

        if (role == null)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("There is not role with this id.");
        } else
        {
            roleService.remove(id);
            responseDTO.setObject(converter.toRoleDTO(role));
            responseDTO.setSuccessMessage("Successfully deleted.");
        }

        return ResponseEntity.ok(responseDTO);
    }

    private ResponseDTO<RoleDTO> getResponse (Role role, String parameter)
    {
        ResponseDTO<RoleDTO> responseDTO = new ResponseDTO<>();
        if (role == null)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("There is not Role with this " + parameter);
        } else
        {
            responseDTO.setObject(converter.toRoleDTO(role));
            responseDTO.setSuccessMessage("Successfully fetched role.");
        }

        return responseDTO;
    }
}
