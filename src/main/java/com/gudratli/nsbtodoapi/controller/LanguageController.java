package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.LanguageDTO;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.entity.Language;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateLanguageException;
import com.gudratli.nsbtodoapi.service.inter.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/language")
public class LanguageController
{
    private final LanguageService languageService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    public ResponseEntity<ResponseDTO<List<LanguageDTO>>> getAll ()
    {
        List<Language> languages = languageService.getAll();

        return ResponseEntity.ok(getResponseWithList(languages));
    }

    @GetMapping("/getByNameContaining/{name}")
    public ResponseEntity<ResponseDTO<List<LanguageDTO>>> getByNameContaining (@PathVariable String name)
    {
        List<Language> languages = languageService.getByNameContaining(name);

        return ResponseEntity.ok(getResponseWithList(languages));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    public ResponseEntity<ResponseDTO<LanguageDTO>> getById (@PathVariable Integer id)
    {
        Language language = languageService.getById(id);

        return ResponseEntity.ok(getResponse(language, "id."));
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<ResponseDTO<LanguageDTO>> getByName (@PathVariable String name)
    {
        Language language = languageService.getByName(name);

        return ResponseEntity.ok(getResponse(language, "name."));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<LanguageDTO>> add (@RequestBody LanguageDTO languageDTO)
    {
        Language language = converter.toLanguage(languageDTO);
        language.setId(null);

        ResponseDTO<LanguageDTO> responseDTO = new ResponseDTO<>();

        try
        {
            language = languageService.add(language);
            responseDTO.setObject(converter.toLanguageDTO(language));
            responseDTO.setSuccessMessage("Successfully inserted data.");
        } catch (DuplicateLanguageException e)
        {
            responseDTO.setErrorCode(304);
            responseDTO.setErrorMessage(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<LanguageDTO>> update (@RequestBody LanguageDTO languageDTO)
    {
        Language language = languageService.getById(languageDTO.getId());
        ResponseDTO<LanguageDTO> responseDTO = new ResponseDTO<>();

        if (language == null)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("There is not language with this id.");
            return ResponseEntity.ok(responseDTO);
        }

        converter.toLanguage(language, languageDTO);

        try
        {
            language = languageService.update(language);
            responseDTO.setObject(converter.toLanguageDTO(language));
            responseDTO.setSuccessMessage("Successfully updated data.");
        } catch (DuplicateLanguageException e)
        {
            responseDTO.setErrorCode(304);
            responseDTO.setErrorMessage(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<LanguageDTO>> delete (@PathVariable Integer id)
    {
        Language language = languageService.getById(id);
        ResponseDTO<LanguageDTO> responseDTO = new ResponseDTO<>();

        if (language == null)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("There is not language with this id.");
        } else
        {
            languageService.remove(id);
            responseDTO.setObject(converter.toLanguageDTO(language));
            responseDTO.setSuccessMessage("Successfully deleted.");
        }

        return ResponseEntity.ok(responseDTO);
    }

    private ResponseDTO<List<LanguageDTO>> getResponseWithList (List<Language> languages)
    {
        List<LanguageDTO> languageDTOs = new ArrayList<>();

        languages.forEach(language -> languageDTOs.add(converter.toLanguageDTO(language)));

        ResponseDTO<List<LanguageDTO>> responseDTO = new ResponseDTO<>(languageDTOs);
        responseDTO.setSuccessMessage("Successfully fetched data.");

        return responseDTO;
    }

    private ResponseDTO<LanguageDTO> getResponse (Language language, String parameter)
    {
        ResponseDTO<LanguageDTO> responseDTO = new ResponseDTO<>();

        if (language == null)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("There is not language with this " + parameter);
        } else
        {
            responseDTO.setObject(converter.toLanguageDTO(language));
            responseDTO.setSuccessMessage("Successfully fetched data.");
        }

        return responseDTO;
    }
}