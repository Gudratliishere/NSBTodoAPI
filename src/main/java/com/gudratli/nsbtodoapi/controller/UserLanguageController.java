package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.dto.UserLanguageDTO;
import com.gudratli.nsbtodoapi.entity.UserLanguage;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateUserLanguageException;
import com.gudratli.nsbtodoapi.service.inter.UserLanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userLanguage")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
public class UserLanguageController
{
    private final UserLanguageService userLanguageService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    public ResponseEntity<ResponseDTO<List<UserLanguageDTO>>> getAll ()
    {
        List<UserLanguage> userLanguages = userLanguageService.getAll();

        return ResponseEntity.ok(getResponseWithList(userLanguages));
    }

    @GetMapping("/getByUserId/{id}")
    public ResponseEntity<ResponseDTO<List<UserLanguageDTO>>> getByUserId (@PathVariable Integer id)
    {
        List<UserLanguage> userLanguages = userLanguageService.getByUserId(id);

        return ResponseEntity.ok(getResponseWithList(userLanguages));
    }

    @GetMapping("/getByLanguageId/{id}")
    public ResponseEntity<ResponseDTO<List<UserLanguageDTO>>> getByLanguageId (@PathVariable Integer id)
    {
        List<UserLanguage> userLanguages = userLanguageService.getByLanguageId(id);

        return ResponseEntity.ok(getResponseWithList(userLanguages));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    public ResponseEntity<ResponseDTO<UserLanguageDTO>> getById (@PathVariable Integer id)
    {
        UserLanguage userLanguage = userLanguageService.getById(id);
        ResponseDTO<UserLanguageDTO> responseDTO = new ResponseDTO<>();

        if (userLanguage == null)
            return ResponseEntity.ok(responseDTO.notFound("userLanguage", "id."));

        return ResponseEntity.ok(responseDTO.successfullyFetched(converter.toUserLanguageDTO(userLanguage)));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<UserLanguageDTO>> add (@RequestBody UserLanguageDTO userLanguageDTO)
    {
        ResponseDTO<UserLanguageDTO> responseDTO = new ResponseDTO<>();

        UserLanguage userLanguage;
        try
        {
            userLanguage = converter.toUserLanguage(userLanguageDTO);
        } catch (Exception e)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.ok(responseDTO);
        }
        userLanguage.setId(null);


        try
        {
            userLanguage = userLanguageService.add(userLanguage);
            responseDTO.successfullyInserted(converter.toUserLanguageDTO(userLanguage));
        } catch (DuplicateUserLanguageException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<UserLanguageDTO>> update (@RequestBody UserLanguageDTO userLanguageDTO)
    {

        UserLanguage userLanguage = userLanguageService.getById(userLanguageDTO.getId());
        ResponseDTO<UserLanguageDTO> responseDTO = new ResponseDTO<>();

        if (userLanguage == null)
            return ResponseEntity.ok(responseDTO.notFound("userLanguage", "id."));

        try
        {
            converter.toUserLanguage(userLanguage, userLanguageDTO);
        } catch (Exception e)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.ok(responseDTO);
        }

        try
        {
            userLanguage = userLanguageService.update(userLanguage);
            responseDTO.successfullyInserted(converter.toUserLanguageDTO(userLanguage));
        } catch (DuplicateUserLanguageException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserLanguageDTO>> delete (@PathVariable Integer id)
    {
        UserLanguage userLanguage = userLanguageService.getById(id);
        ResponseDTO<UserLanguageDTO> responseDTO = new ResponseDTO<>();

        if (userLanguage == null)
            return ResponseEntity.ok(responseDTO.notFound("userLanguage", "id."));

        userLanguageService.remove(id);

        return ResponseEntity.ok(responseDTO.successfullyDeleted(converter.toUserLanguageDTO(userLanguage)));
    }

    private ResponseDTO<List<UserLanguageDTO>> getResponseWithList (List<UserLanguage> userLanguages)
    {
        List<UserLanguageDTO> userLanguageDTOs = new ArrayList<>();

        userLanguages.forEach(userLanguage -> userLanguageDTOs.add(converter.toUserLanguageDTO(userLanguage)));

        ResponseDTO<List<UserLanguageDTO>> responseDTO = new ResponseDTO<>();
        return responseDTO.successfullyFetched(userLanguageDTOs);
    }
}