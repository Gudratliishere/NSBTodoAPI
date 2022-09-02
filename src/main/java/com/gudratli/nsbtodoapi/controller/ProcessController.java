package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.ProcessDTO;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.entity.Process;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateProcessException;
import com.gudratli.nsbtodoapi.service.inter.ProcessService;
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
@RequestMapping("/process")
@Api("Process controller")
public class ProcessController
{
    private final ProcessService processService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Get All", notes = "Returns all processes.")
    public ResponseEntity<ResponseDTO<List<ProcessDTO>>> getAll ()
    {
        List<Process> processes = processService.getAll();

        return ResponseEntity.ok(getResponseWithList(processes));
    }

    @GetMapping("/getByUserId/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Get by user ID", notes = "Returns all processes of that user.")
    public ResponseEntity<ResponseDTO<List<ProcessDTO>>> getByUserId (@PathVariable @ApiParam(name = "ID",
            value = "ID of the user.", required = true, example = "12") Integer id)
    {
        List<Process> processes = processService.getByUserId(id);

        return ResponseEntity.ok(getResponseWithList(processes));
    }

    @GetMapping("/getByTaskId/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Get by task ID", notes = "Returns all processes of that task.")
    public ResponseEntity<ResponseDTO<List<ProcessDTO>>> getByTaskId (@PathVariable @ApiParam(name = "ID",
            value = "ID of the task.", required = true, example = "10") Integer id)
    {
        List<Process> processes = processService.getByTaskId(id);

        return ResponseEntity.ok(getResponseWithList(processes));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Get by ID", notes = "Returns single process according to ID.")
    public ResponseEntity<ResponseDTO<ProcessDTO>> getById (@PathVariable @ApiParam(name = "ID",
            value = "ID of the process.", required = true, example = "15") Integer id)
    {
        Process process = processService.getById(id);
        ResponseDTO<ProcessDTO> responseDTO = new ResponseDTO<>();

        if (process == null)
            return ResponseEntity.ok(responseDTO.notFound("process", "id."));

        return ResponseEntity.ok(responseDTO.successfullyFetched(converter.toProcessDTO(process)));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Add", notes = "Add new process.")
    public ResponseEntity<ResponseDTO<ProcessDTO>> add (@Valid @RequestBody @ApiParam(name = "Process",
            value = "DTO for process", required = true) ProcessDTO processDTO)
    {
        ResponseDTO<ProcessDTO> responseDTO = new ResponseDTO<>(processDTO);

        Process process;
        try
        {
            process = converter.toProcess(processDTO);
        } catch (Exception e)
        {
            return ResponseEntity.ok(responseDTO.notFound(e.getMessage()));
        }
        process.setId(null);

        try
        {
            process = processService.add(process);
            responseDTO.successfullyInserted(converter.toProcessDTO(process));
        } catch (DuplicateProcessException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Update", notes = "Update existing process.")
    public ResponseEntity<ResponseDTO<ProcessDTO>> update (@Valid @RequestBody @ApiParam(name = "Process",
            value = "DTO for process.", required = true) ProcessDTO processDTO)
    {
        ResponseDTO<ProcessDTO> responseDTO = new ResponseDTO<>(processDTO);

        Process process = processService.getById(processDTO.getId());
        if (process == null)
            return ResponseEntity.ok(responseDTO.notFound("process", "id."));

        try
        {
            converter.toProcess(process, processDTO);
        } catch (Exception e)
        {
            return ResponseEntity.ok(responseDTO.notFound(e.getMessage()));
        }

        try
        {
            process = processService.update(process);
            responseDTO.successfullyUpdated(converter.toProcessDTO(process));
        } catch (DuplicateProcessException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Delete", notes = "Delete single process according to it's ID.")
    public ResponseEntity<ResponseDTO<ProcessDTO>> delete (@PathVariable @ApiParam(name = "ID",
            value = "ID of the process", required = true, example = "12") Integer id)
    {
        ResponseDTO<ProcessDTO> responseDTO = new ResponseDTO<>();
        Process process = processService.getById(id);
        if (process == null)
            return ResponseEntity.ok(responseDTO.notFound("process", "id."));

        processService.remove(id);

        return ResponseEntity.ok(responseDTO.successfullyDeleted(converter.toProcessDTO(process)));
    }

    private ResponseDTO<List<ProcessDTO>> getResponseWithList (List<Process> processes)
    {
        List<ProcessDTO> processDTOs = new ArrayList<>();

        processes.forEach(process -> processDTOs.add(converter.toProcessDTO(process)));

        ResponseDTO<List<ProcessDTO>> responseDTO = new ResponseDTO<>();
        return responseDTO.successfullyFetched(processDTOs);
    }
}