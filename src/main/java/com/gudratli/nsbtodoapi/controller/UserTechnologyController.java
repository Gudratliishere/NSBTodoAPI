package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.dto.UserTechnologyDTO;
import com.gudratli.nsbtodoapi.entity.UserTechnology;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateUserTechnologyException;
import com.gudratli.nsbtodoapi.service.inter.UserTechnologyService;
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
@RequestMapping("/userTechnology")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
@Api("User technology controller")
public class UserTechnologyController
{
    private final UserTechnologyService userTechnologyService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    @ApiOperation(value = "Get All", notes = "Returns all user technologies.")
    public ResponseEntity<ResponseDTO<List<UserTechnologyDTO>>> getAll ()
    {
        List<UserTechnology> userTechnologies = userTechnologyService.getAll();

        return ResponseEntity.ok(getResponseWithList(userTechnologies));
    }

    @GetMapping("/getByUserId/{id}")
    @ApiOperation(value = "Get by user ID", notes = "Returns all technologies that user knows.")
    public ResponseEntity<ResponseDTO<List<UserTechnologyDTO>>> getByUserId (@PathVariable @ApiParam(name = "ID",
            value = "ID of user", required = true, example = "26") Integer id)
    {
        List<UserTechnology> userTechnologies = userTechnologyService.getByUserId(id);

        return ResponseEntity.ok(getResponseWithList(userTechnologies));
    }

    @GetMapping("/getByTechnologyId/{id}")
    @ApiOperation(value = "Get by technology ID.", notes = "Returns all users who know that technology/")
    public ResponseEntity<ResponseDTO<List<UserTechnologyDTO>>> getByTechnologyId (@PathVariable @ApiParam(name = "ID",
            value = "ID of technology", required = true, example = "21") Integer id)
    {
        List<UserTechnology> userTechnologies = userTechnologyService.getByTechnologyId(id);

        return ResponseEntity.ok(getResponseWithList(userTechnologies));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    @ApiOperation(value = "Get by user ID", notes = "Get single user technology with it's ID.")
    public ResponseEntity<ResponseDTO<UserTechnologyDTO>> getById (@PathVariable @ApiParam(name = "ID",
            value = "ID of user technology", required = true, example = "16") Integer id)
    {
        UserTechnology userTechnology = userTechnologyService.getById(id);
        ResponseDTO<UserTechnologyDTO> responseDTO = new ResponseDTO<>();

        if (userTechnology == null)
            return ResponseEntity.ok(responseDTO.notFound("userTechnology", "id."));

        return ResponseEntity.ok(responseDTO.successfullyFetched(converter.toUserTechnologyDTO(userTechnology)));
    }

    @PostMapping
    @ApiOperation(value = "Add", notes = "Add new user technology")
    public ResponseEntity<ResponseDTO<UserTechnologyDTO>> add (@Valid @RequestBody @ApiParam(name = "User technology",
            value = "DTO for user technology", required = true) UserTechnologyDTO userTechnologyDTO)
    {
        ResponseDTO<UserTechnologyDTO> responseDTO = new ResponseDTO<>();

        UserTechnology userTechnology;
        try
        {
            userTechnology = converter.toUserTechnology(userTechnologyDTO);
        } catch (Exception e)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.ok(responseDTO);
        }
        userTechnology.setId(null);

        try
        {
            userTechnology = userTechnologyService.add(userTechnology);
            responseDTO.successfullyInserted(converter.toUserTechnologyDTO(userTechnology));
        } catch (DuplicateUserTechnologyException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping
    @ApiOperation(value = "Update", notes = "Update existing user technology")
    public ResponseEntity<ResponseDTO<UserTechnologyDTO>> update (
            @Valid @RequestBody @ApiParam(name = "User technology",
                    value = "DTO for user technology", required = true) UserTechnologyDTO userTechnologyDTO)
    {
        ResponseDTO<UserTechnologyDTO> responseDTO = new ResponseDTO<>(userTechnologyDTO);
        UserTechnology userTechnology = userTechnologyService.getById(userTechnologyDTO.getId());

        if (userTechnology == null)
            return ResponseEntity.ok(responseDTO.notFound("userTechnology", "id."));

        try
        {
            converter.toUserTechnology(userTechnology, userTechnologyDTO);
        } catch (Exception e)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.ok(responseDTO);
        }

        try
        {
            userTechnology = userTechnologyService.update(userTechnology);
            responseDTO.successfullyUpdated(converter.toUserTechnologyDTO(userTechnology));
        } catch (DuplicateUserTechnologyException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete", notes = "Delete single user technology according to it's ID.")
    public ResponseEntity<ResponseDTO<UserTechnologyDTO>> delete (@PathVariable @ApiParam(name = "ID",
            value = "ID of user technology", required = true, example = "45") Integer id)
    {
        UserTechnology userTechnology = userTechnologyService.getById(id);
        ResponseDTO<UserTechnologyDTO> responseDTO = new ResponseDTO<>();

        if (userTechnology == null)
            return ResponseEntity.ok(responseDTO.notFound("userTechnology", "id."));

        userTechnologyService.remove(id);
        return ResponseEntity.ok(responseDTO.successfullyDeleted(converter.toUserTechnologyDTO(userTechnology)));
    }

    private ResponseDTO<List<UserTechnologyDTO>> getResponseWithList (List<UserTechnology> userTechnologies)
    {
        List<UserTechnologyDTO> userTechnologyDTOs = new ArrayList<>();

        userTechnologies.forEach(
                userTechnology -> userTechnologyDTOs.add(converter.toUserTechnologyDTO(userTechnology)));

        ResponseDTO<List<UserTechnologyDTO>> responseDTO = new ResponseDTO<>();
        return responseDTO.successfullyFetched(userTechnologyDTOs);
    }
}