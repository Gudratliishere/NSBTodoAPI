package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.dto.RoleDTO;
import com.gudratli.nsbtodoapi.entity.Role;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateRoleException;
import com.gudratli.nsbtodoapi.service.inter.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
@Api("Role controller")
public class RoleController
{
    private final RoleService roleService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Get All", notes = "Returns all roles.")
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
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Get by ID", notes = "Returns single role according to it's ID.")
    public ResponseEntity<ResponseDTO<RoleDTO>> getById (@PathVariable @ApiParam(name = "ID",
            value = "ID of the role", required = true, example = "1") Integer id)
    {
        Role role = roleService.getById(id);

        return ResponseEntity.ok(getResponse(role, "id"));
    }

    @GetMapping("/getByName/{name}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Get by ID", notes = "Returns single role according to it's name.")
    public ResponseEntity<ResponseDTO<RoleDTO>> getByName (@PathVariable @ApiParam(name = "Name",
            value = "Name of the role", required = true, example = "USER") String name)
    {
        Role role = roleService.getByName(name);

        return ResponseEntity.ok(getResponse(role, "name"));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Add", notes = "Add new role.")
    public ResponseEntity<ResponseDTO<RoleDTO>> add (@Valid @RequestBody @ApiParam(name = "Role",
            value = "DTO for role", required = true) RoleDTO roleDTO)
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
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Update", notes = "Update single role with it's ID.")
    public ResponseEntity<ResponseDTO<RoleDTO>> update (@Valid @RequestBody @ApiParam(name = "Role",
            value = "DTO for roles.", required = true) RoleDTO roleDTO)
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
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Delete", notes = "Delete single role with it's ID.")
    public ResponseEntity<ResponseDTO<RoleDTO>> delete (@PathVariable @ApiParam(name = "ID",
            value = "ID of the role", required = true, example = "3") Integer id)
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