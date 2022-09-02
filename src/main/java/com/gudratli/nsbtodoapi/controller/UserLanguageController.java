package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.dto.UserLanguageDTO;
import com.gudratli.nsbtodoapi.entity.UserLanguage;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateUserLanguageException;
import com.gudratli.nsbtodoapi.service.inter.UserLanguageService;
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
@RequestMapping("/userLanguage")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
@Api("User language controller")
public class UserLanguageController
{
    private final UserLanguageService userLanguageService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    @ApiOperation(value = "Get All", notes = "Returns all user languages.")
    public ResponseEntity<ResponseDTO<List<UserLanguageDTO>>> getAll ()
    {
        List<UserLanguage> userLanguages = userLanguageService.getAll();

        return ResponseEntity.ok(getResponseWithList(userLanguages));
    }

    @GetMapping("/getByUserId/{id}")
    @ApiOperation(value = "Get by user ID", notes = "Get all languages of user.")
    public ResponseEntity<ResponseDTO<List<UserLanguageDTO>>> getByUserId (@PathVariable @ApiParam(name = "ID",
            value = "ID of user", required = true, example = "35") Integer id)
    {
        List<UserLanguage> userLanguages = userLanguageService.getByUserId(id);

        return ResponseEntity.ok(getResponseWithList(userLanguages));
    }

    @GetMapping("/getByLanguageId/{id}")
    @ApiOperation(value = "Get by language ID", notes = "Get all users who knows that language.")
    public ResponseEntity<ResponseDTO<List<UserLanguageDTO>>> getByLanguageId (@PathVariable @ApiParam(name = "ID",
            value = "ID of langauge", required = true, example = "23") Integer id)
    {
        List<UserLanguage> userLanguages = userLanguageService.getByLanguageId(id);

        return ResponseEntity.ok(getResponseWithList(userLanguages));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    @ApiOperation(value = "Get by ID", notes = "Get single user language according to it's ID.")
    public ResponseEntity<ResponseDTO<UserLanguageDTO>> getById (@PathVariable @ApiParam(name = "ID",
            value = "ID of user language", required = true, example = "29") Integer id)
    {
        UserLanguage userLanguage = userLanguageService.getById(id);
        ResponseDTO<UserLanguageDTO> responseDTO = new ResponseDTO<>();

        if (userLanguage == null)
            return ResponseEntity.ok(responseDTO.notFound("userLanguage", "id."));

        return ResponseEntity.ok(responseDTO.successfullyFetched(converter.toUserLanguageDTO(userLanguage)));
    }

    @PostMapping
    @ApiOperation(value = "Add", notes = "Add new user language")
    public ResponseEntity<ResponseDTO<UserLanguageDTO>> add (@Valid @RequestBody @ApiParam(name = "User language",
            value = "DTO for user language", required = true) UserLanguageDTO userLanguageDTO)
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
    @ApiOperation(value = "Update", notes = "Update existing user language")
    public ResponseEntity<ResponseDTO<UserLanguageDTO>> update (@Valid @RequestBody @ApiParam(name = "User language",
            value = "DTO for user language", required = true) UserLanguageDTO userLanguageDTO)
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
    @ApiOperation(value = "Delete", notes = "Delete single user language with it's ID.")
    public ResponseEntity<ResponseDTO<UserLanguageDTO>> delete (@PathVariable @ApiParam(name = "ID",
            value = "ID of user language", required = true, example = "23") Integer id)
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