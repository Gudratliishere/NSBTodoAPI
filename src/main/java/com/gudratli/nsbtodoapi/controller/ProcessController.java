package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.ProcessDTO;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.entity.Process;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateProcessException;
import com.gudratli.nsbtodoapi.service.inter.ProcessService;
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
public class ProcessController
{
    private final ProcessService processService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseDTO<List<ProcessDTO>>> getAll ()
    {
        List<Process> processes = processService.getAll();

        return ResponseEntity.ok(getResponseWithList(processes));
    }

    @GetMapping("/getByUserId/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<ResponseDTO<List<ProcessDTO>>> getByUserId (@PathVariable Integer id)
    {
        List<Process> processes = processService.getByUserId(id);

        return ResponseEntity.ok(getResponseWithList(processes));
    }

    @GetMapping("/getByTaskId/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<ResponseDTO<List<ProcessDTO>>> getByTaskId (@PathVariable Integer id)
    {
        List<Process> processes = processService.getByTaskId(id);

        return ResponseEntity.ok(getResponseWithList(processes));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<ResponseDTO<ProcessDTO>> getById (@PathVariable Integer id)
    {
        Process process = processService.getById(id);
        ResponseDTO<ProcessDTO> responseDTO = new ResponseDTO<>();

        if (process == null)
            return ResponseEntity.ok(responseDTO.notFound("process", "id."));

        return ResponseEntity.ok(responseDTO.successfullyFetched(converter.toProcessDTO(process)));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseDTO<ProcessDTO>> add (@Valid @RequestBody ProcessDTO processDTO)
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
    public ResponseEntity<ResponseDTO<ProcessDTO>> update (@Valid @RequestBody ProcessDTO processDTO)
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
    public ResponseEntity<ResponseDTO<ProcessDTO>> delete (@PathVariable Integer id)
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