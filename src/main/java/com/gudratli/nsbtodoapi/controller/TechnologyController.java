package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.dto.TechnologyDTO;
import com.gudratli.nsbtodoapi.entity.Technology;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateTechnologyException;
import com.gudratli.nsbtodoapi.service.inter.TechnologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/technology")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
public class TechnologyController
{
    private final TechnologyService technologyService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    public ResponseEntity<ResponseDTO<List<TechnologyDTO>>> getAll ()
    {
        List<Technology> technologies = technologyService.getAll();

        return ResponseEntity.ok(getResponseWithList(technologies));
    }

    @GetMapping("/getByNameContaining/{name}")
    public ResponseEntity<ResponseDTO<List<TechnologyDTO>>> getByNameContaining (@PathVariable String name)
    {
        List<Technology> technologies = technologyService.getByNameContaining(name);

        return ResponseEntity.ok(getResponseWithList(technologies));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    public ResponseEntity<ResponseDTO<TechnologyDTO>> getById (@PathVariable Integer id)
    {
        Technology technology = technologyService.getById(id);

        return ResponseEntity.ok(getResponse(technology, "id."));
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<ResponseDTO<TechnologyDTO>> getByName (@PathVariable String name)
    {
        Technology technology = technologyService.getByName(name);

        return ResponseEntity.ok(getResponse(technology, "name."));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<TechnologyDTO>> add (@Valid @RequestBody TechnologyDTO technologyDTO)
    {
        Technology technology = converter.toTechnology(technologyDTO);
        technology.setId(null);

        ResponseDTO<TechnologyDTO> responseDTO = new ResponseDTO<>(technologyDTO);

        try
        {
            technology = technologyService.add(technology);
            responseDTO.successfullyInserted(converter.toTechnologyDTO(technology));
        } catch (DuplicateTechnologyException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<TechnologyDTO>> update (@Valid @RequestBody TechnologyDTO technologyDTO)
    {
        Technology technology = technologyService.getById(technologyDTO.getId());
        ResponseDTO<TechnologyDTO> responseDTO = new ResponseDTO<>(technologyDTO);

        if (technology == null)
            return ResponseEntity.ok(responseDTO.notFound("technology", "id."));

        converter.toTechnology(technology, technologyDTO);

        try
        {
            technology = technologyService.update(technology);
            responseDTO.successfullyUpdated(converter.toTechnologyDTO(technology));
        } catch (DuplicateTechnologyException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<TechnologyDTO>> delete (@PathVariable Integer id)
    {
        Technology technology = technologyService.getById(id);
        ResponseDTO<TechnologyDTO> responseDTO = new ResponseDTO<>();

        if (technology == null)
            responseDTO.notFound("technology", "id.");
        else
        {
            technologyService.remove(id);
            responseDTO.successfullyDeleted(converter.toTechnologyDTO(technology));
        }

        return ResponseEntity.ok(responseDTO);
    }

    private ResponseDTO<List<TechnologyDTO>> getResponseWithList (List<Technology> technologies)
    {
        List<TechnologyDTO> technologyDTOs = new ArrayList<>();

        technologies.forEach(technology -> technologyDTOs.add(converter.toTechnologyDTO(technology)));

        ResponseDTO<List<TechnologyDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.successfullyFetched(technologyDTOs);

        return responseDTO;
    }

    private ResponseDTO<TechnologyDTO> getResponse (Technology technology, String parameter)
    {
        ResponseDTO<TechnologyDTO> responseDTO = new ResponseDTO<>();

        if (technology == null)
            responseDTO.notFound("technology", parameter);
        else
            responseDTO.successfullyFetched(converter.toTechnologyDTO(technology));

        return responseDTO;
    }
}