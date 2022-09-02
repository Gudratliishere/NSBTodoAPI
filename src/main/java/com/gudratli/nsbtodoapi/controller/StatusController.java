package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.dto.StatusDTO;
import com.gudratli.nsbtodoapi.entity.Status;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateStatusException;
import com.gudratli.nsbtodoapi.service.inter.StatusService;
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
@RequestMapping("/status")
@Api("Status controller")
public class StatusController
{
    private final StatusService statusService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Get All", notes = "Get all status")
    public ResponseEntity<ResponseDTO<List<StatusDTO>>> getAll ()
    {
        List<Status> statuses = statusService.getAll();
        List<StatusDTO> statusDTOs = new ArrayList<>();

        statuses.forEach(status -> statusDTOs.add(converter.toStatusDTO(status)));

        ResponseDTO<List<StatusDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.successfullyFetched(statusDTOs);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Get by ID", notes = "Returns single status with it's ID.")
    public ResponseEntity<ResponseDTO<StatusDTO>> getById (@PathVariable @ApiParam(name = "ID",
            value = "ID of the status", required = true, example = "3") Integer id)
    {
        Status status = statusService.getById(id);

        return ResponseEntity.ok(getResponse(status, "id."));
    }

    @GetMapping("/getByName/{name}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Get by name", notes = "Returns single status with it's name, because name is unique.")
    public ResponseEntity<ResponseDTO<StatusDTO>> getByName (@PathVariable @ApiParam(name = "Name",
            value = "Name of the status", required = true, example = "Incomplete") String name)
    {
        Status status = statusService.getByName(name);

        return ResponseEntity.ok(getResponse(status, "name."));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Add", notes = "Add new status")
    public ResponseEntity<ResponseDTO<StatusDTO>> add (@Valid @RequestBody @ApiParam(name = "Status",
            value = "DTO for status", required = true) StatusDTO statusDTO)
    {
        Status status = converter.toStatus(statusDTO);
        status.setId(null);

        ResponseDTO<StatusDTO> responseDTO = new ResponseDTO<>();

        try
        {
            status = statusService.add(status);
            responseDTO.successfullyInserted(converter.toStatusDTO(status));
        } catch (DuplicateStatusException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Update", notes = "Update existing status with it's ID.")
    public ResponseEntity<ResponseDTO<StatusDTO>> update (@Valid @RequestBody @ApiParam(name = "Status",
            value = "DTO for status", required = true) StatusDTO statusDTO)
    {
        Status status = statusService.getById(statusDTO.getId());
        ResponseDTO<StatusDTO> responseDTO = new ResponseDTO<>(statusDTO);

        if (status == null)
            return ResponseEntity.ok(responseDTO.notFound("status", "id."));

        converter.toStatus(status, statusDTO);

        try
        {
            status = statusService.update(status);
            responseDTO.successfullyUpdated(converter.toStatusDTO(status));
        } catch (DuplicateStatusException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Delete", notes = "Delete single status with it's ID.")
    public ResponseEntity<ResponseDTO<StatusDTO>> delete (@PathVariable @ApiParam(name = "ID",
            value = "ID of the status", required = true, example = "5") Integer id)
    {
        Status status = statusService.getById(id);
        ResponseDTO<StatusDTO> responseDTO = new ResponseDTO<>();

        if (status == null)
            responseDTO.notFound("status", "id.");
        else
        {
            statusService.remove(id);
            responseDTO.successfullyDeleted(converter.toStatusDTO(status));
        }

        return ResponseEntity.ok(responseDTO);
    }

    private ResponseDTO<StatusDTO> getResponse (Status status, String parameter)
    {
        ResponseDTO<StatusDTO> responseDTO = new ResponseDTO<>();

        if (status == null)
            responseDTO.notFound("status", parameter);
        else
            responseDTO.successfullyFetched(converter.toStatusDTO(status));

        return responseDTO;
    }
}