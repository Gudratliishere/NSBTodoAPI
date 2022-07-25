package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.dto.StatusDTO;
import com.gudratli.nsbtodoapi.entity.Status;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateStatusException;
import com.gudratli.nsbtodoapi.service.inter.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/status")
public class StatusController
{
    private final StatusService statusService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    public ResponseEntity<ResponseDTO<List<StatusDTO>>> getAll ()
    {
        List<Status> statuses = statusService.getAll();
        List<StatusDTO> statusDTOs = new ArrayList<>();

        statuses.forEach(status -> statusDTOs.add(converter.toStatusDTO(status)));

        ResponseDTO<List<StatusDTO>> responseDTO = new ResponseDTO<>(statusDTOs);
        responseDTO.setSuccessMessage("Successfully fetched all data.");

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    public ResponseEntity<ResponseDTO<StatusDTO>> getById (@PathVariable Integer id)
    {
        Status status = statusService.getById(id);

        return ResponseEntity.ok(getResponse(status, "id."));
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<ResponseDTO<StatusDTO>> getByName (@PathVariable String name)
    {
        Status status = statusService.getByName(name);

        return ResponseEntity.ok(getResponse(status, "name."));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<StatusDTO>> add (@RequestBody StatusDTO statusDTO)
    {
        Status status = converter.toStatus(statusDTO);
        status.setId(null);

        ResponseDTO<StatusDTO> responseDTO = new ResponseDTO<>();

        try
        {
            status = statusService.add(status);
            responseDTO.setObject(converter.toStatusDTO(status));
            responseDTO.setSuccessMessage("Successfully inserted data.");
        } catch (DuplicateStatusException e)
        {
            responseDTO.setErrorCode(304);
            responseDTO.setErrorMessage(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<StatusDTO>> update (@RequestBody StatusDTO statusDTO)
    {
        Status status = statusService.getById(statusDTO.getId());
        ResponseDTO<StatusDTO> responseDTO = new ResponseDTO<>(statusDTO);

        if (status == null)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("There is not status with this id.");
            return ResponseEntity.ok(responseDTO);
        }

        converter.toStatus(status, statusDTO);

        try
        {
            status = statusService.update(status);
            responseDTO.setObject(converter.toStatusDTO(status));
            responseDTO.setSuccessMessage("Successfully updated data.");
        } catch (DuplicateStatusException e)
        {
            responseDTO.setErrorCode(304);
            responseDTO.setErrorMessage(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<StatusDTO>> delete (@PathVariable Integer id)
    {
        Status status = statusService.getById(id);
        ResponseDTO<StatusDTO> responseDTO = new ResponseDTO<>();

        if (status == null)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("There is not status with this id.");
        } else
        {
            statusService.remove(id);
            responseDTO.setObject(converter.toStatusDTO(status));
            responseDTO.setSuccessMessage("Successfully deleted.");
        }

        return ResponseEntity.ok(responseDTO);
    }

    private ResponseDTO<StatusDTO> getResponse (Status status, String parameter)
    {
        ResponseDTO<StatusDTO> responseDTO = new ResponseDTO<>();

        if (status == null)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("There is not status with this " + parameter);
        } else
        {
            responseDTO.setObject(converter.toStatusDTO(status));
            responseDTO.setSuccessMessage("Successfully fetched data.");
        }

        return responseDTO;
    }
}