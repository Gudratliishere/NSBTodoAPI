package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.LanguageDTO;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.entity.Language;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateLanguageException;
import com.gudratli.nsbtodoapi.service.inter.LanguageService;
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
@RequestMapping("/language")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
@Api("Language controller")
public class LanguageController
{
    private final LanguageService languageService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    @ApiOperation(value = "Get All", notes = "Returns all languages.")
    public ResponseEntity<ResponseDTO<List<LanguageDTO>>> getAll ()
    {
        List<Language> languages = languageService.getAll();

        return ResponseEntity.ok(getResponseWithList(languages));
    }

    @GetMapping("/getByNameContaining/{name}")
    @ApiOperation(value = "Get by name containing", notes = "Returns list of languages that contains that key.")
    public ResponseEntity<ResponseDTO<List<LanguageDTO>>> getByNameContaining (@PathVariable @ApiParam(name = "name",
            value = "Name that you think languages contain.", required = true, example = "Eng") String name)
    {
        List<Language> languages = languageService.getByNameContaining(name);

        return ResponseEntity.ok(getResponseWithList(languages));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    @ApiOperation(value = "Get by ID", notes = "Returns single language according to ID.")
    public ResponseEntity<ResponseDTO<LanguageDTO>> getById (@PathVariable @ApiParam(name = "ID",
            value = "ID of language", required = true, example = "15") Integer id)
    {
        Language language = languageService.getById(id);

        return ResponseEntity.ok(getResponse(language, "id."));
    }

    @GetMapping("/getByName/{name}")
    @ApiOperation(value = "Get by name", notes = "Returns single Language according to name, because name is unique.")
    public ResponseEntity<ResponseDTO<LanguageDTO>> getByName (@PathVariable @ApiParam(name = "name",
            value = "Name of language.", required = true, example = "English") String name)
    {
        Language language = languageService.getByName(name);

        return ResponseEntity.ok(getResponse(language, "name."));
    }

    @PostMapping
    @ApiOperation(value = "Add", notes = "Add new language.")
    public ResponseEntity<ResponseDTO<LanguageDTO>> add (@Valid @RequestBody @ApiParam(name = "Language",
            value = "DTO that contains language name.", required = true) LanguageDTO languageDTO)
    {
        Language language = converter.toLanguage(languageDTO);
        language.setId(null);

        ResponseDTO<LanguageDTO> responseDTO = new ResponseDTO<>();

        try
        {
            language = languageService.add(language);
            responseDTO.successfullyInserted(converter.toLanguageDTO(language));
        } catch (DuplicateLanguageException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping
    @ApiOperation(value = "Update", notes = "Update existing language with it's ID")
    public ResponseEntity<ResponseDTO<LanguageDTO>> update (@Valid @RequestBody @ApiParam(name = "Language",
            value = "DTO that contains language name and ID", required = true) LanguageDTO languageDTO)
    {
        Language language = languageService.getById(languageDTO.getId());
        ResponseDTO<LanguageDTO> responseDTO = new ResponseDTO<>();

        if (language == null)
            return ResponseEntity.ok(responseDTO.notFound("language", "id"));

        converter.toLanguage(language, languageDTO);

        try
        {
            language = languageService.update(language);
            responseDTO.successfullyUpdated(converter.toLanguageDTO(language));
        } catch (DuplicateLanguageException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete", notes = "Deletes language according to ID.")
    public ResponseEntity<ResponseDTO<LanguageDTO>> delete (@PathVariable @ApiParam(name = "ID",
            value = "ID of language", required = true, example = "19") Integer id)
    {
        Language language = languageService.getById(id);
        ResponseDTO<LanguageDTO> responseDTO = new ResponseDTO<>();

        if (language == null)
            responseDTO.notFound("language", "id.");
        else
        {
            languageService.remove(id);
            responseDTO.successfullyDeleted(converter.toLanguageDTO(language));
        }

        return ResponseEntity.ok(responseDTO);
    }

    private ResponseDTO<List<LanguageDTO>> getResponseWithList (List<Language> languages)
    {
        List<LanguageDTO> languageDTOs = new ArrayList<>();

        languages.forEach(language -> languageDTOs.add(converter.toLanguageDTO(language)));

        ResponseDTO<List<LanguageDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.successfullyFetched(languageDTOs);

        return responseDTO;
    }

    private ResponseDTO<LanguageDTO> getResponse (Language language, String parameter)
    {
        ResponseDTO<LanguageDTO> responseDTO = new ResponseDTO<>();

        if (language == null)
            responseDTO.notFound("language", parameter);
        else
            responseDTO.successfullyFetched(converter.toLanguageDTO(language));

        return responseDTO;
    }
}