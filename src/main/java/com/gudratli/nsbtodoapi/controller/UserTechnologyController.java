package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.dto.UserTechnologyDTO;
import com.gudratli.nsbtodoapi.entity.UserTechnology;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateUserTechnologyException;
import com.gudratli.nsbtodoapi.service.inter.UserTechnologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userTechnology")
public class UserTechnologyController
{
    private final UserTechnologyService userTechnologyService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    public ResponseEntity<ResponseDTO<List<UserTechnologyDTO>>> getAll ()
    {
        List<UserTechnology> userTechnologies = userTechnologyService.getAll();

        return ResponseEntity.ok(getResponseWithList(userTechnologies));
    }

    @GetMapping("/getByUserId/{id}")
    public ResponseEntity<ResponseDTO<List<UserTechnologyDTO>>> getByUserId (@PathVariable Integer id)
    {
        List<UserTechnology> userTechnologies = userTechnologyService.getByUserId(id);

        return ResponseEntity.ok(getResponseWithList(userTechnologies));
    }

    @GetMapping("/getByTechnologyId/{id}")
    public ResponseEntity<ResponseDTO<List<UserTechnologyDTO>>> getByTechnologyId (@PathVariable Integer id)
    {
        List<UserTechnology> userTechnologies = userTechnologyService.getByTechnologyId(id);

        return ResponseEntity.ok(getResponseWithList(userTechnologies));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    public ResponseEntity<ResponseDTO<UserTechnologyDTO>> getById (@PathVariable Integer id)
    {
        UserTechnology userTechnology = userTechnologyService.getById(id);
        ResponseDTO<UserTechnologyDTO> responseDTO = new ResponseDTO<>();

        if (userTechnology == null)
            return ResponseEntity.ok(responseDTO.notFound("userTechnology", "id."));

        return ResponseEntity.ok(responseDTO.successfullyFetched(converter.toUserTechnologyDTO(userTechnology)));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<UserTechnologyDTO>> add (@RequestBody UserTechnologyDTO userTechnologyDTO)
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
    public ResponseEntity<ResponseDTO<UserTechnologyDTO>> update (@RequestBody UserTechnologyDTO userTechnologyDTO)
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
    public ResponseEntity<ResponseDTO<UserTechnologyDTO>> delete (@PathVariable Integer id)
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