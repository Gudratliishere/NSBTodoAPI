package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.CountryDTO;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.entity.Country;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateCountryException;
import com.gudratli.nsbtodoapi.service.inter.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/country")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
public class CountryController
{
    private final CountryService countryService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    public ResponseEntity<ResponseDTO<List<CountryDTO>>> getAll ()
    {
        List<Country> countries = countryService.getAll();

        return ResponseEntity.ok(getResponseArray(countries));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    public ResponseEntity<ResponseDTO<CountryDTO>> getById (@PathVariable Integer id)
    {
        Country country = countryService.getById(id);

        return ResponseEntity.ok(getResponseSingle(country, "id"));
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<ResponseDTO<CountryDTO>> getByName (@PathVariable String name)
    {
        Country country = countryService.getByName(name);

        return ResponseEntity.ok(getResponseSingle(country, "name."));
    }

    @GetMapping("/getByNameContaining/{name}")
    public ResponseEntity<ResponseDTO<List<CountryDTO>>> getByNameContaining (@PathVariable String name)
    {
        List<Country> countries = countryService.getByNameContaining(name);

        return ResponseEntity.ok(getResponseArray(countries));
    }

    @GetMapping("/getByRegionId/{id}")
    public ResponseEntity<ResponseDTO<List<CountryDTO>>> getByRegionId (@PathVariable Integer id)
    {
        List<Country> countries = countryService.getByRegionId(id);

        return ResponseEntity.ok(getResponseArray(countries));
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<CountryDTO>> add (@Valid @RequestBody CountryDTO countryDTO)
    {
        Country country = converter.toCountry(countryDTO);
        country.setId(null);

        ResponseDTO<CountryDTO> responseDTO = new ResponseDTO<>();

        try
        {
            country = countryService.add(country);
            responseDTO.successfullyInserted(converter.toCountryDTO(country));
        } catch (DuplicateCountryException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping()
    public ResponseEntity<ResponseDTO<CountryDTO>> update (@Valid @RequestBody CountryDTO countryDTO)
    {
        Country country = countryService.getById(countryDTO.getId());
        ResponseDTO<CountryDTO> responseDTO = new ResponseDTO<>();

        if (country == null)
            return ResponseEntity.ok(responseDTO.notFound("language", "id"));

        converter.toCountry(country, countryDTO);

        try
        {
            country = countryService.update(country);
            responseDTO.successfullyUpdated(converter.toCountryDTO(country));
        } catch (DuplicateCountryException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<CountryDTO>> delete (@PathVariable Integer id)
    {
        Country country = countryService.getById(id);
        ResponseDTO<CountryDTO> responseDTO = new ResponseDTO<>();

        if (country == null)
            responseDTO.notFound("language", "id");
        else
        {
            countryService.remove(id);
            responseDTO.successfullyDeleted(converter.toCountryDTO(country));
        }

        return ResponseEntity.ok(responseDTO);
    }

    private ResponseDTO<List<CountryDTO>> getResponseArray (List<Country> countries)
    {
        List<CountryDTO> countryDTOs = new ArrayList<>();

        countries.forEach(country -> countryDTOs.add(converter.toCountryDTO(country)));

        ResponseDTO<List<CountryDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.successfullyFetched(countryDTOs);

        return responseDTO;
    }

    private ResponseDTO<CountryDTO> getResponseSingle (Country country, String parameter)
    {
        ResponseDTO<CountryDTO> responseDTO = new ResponseDTO<>();
        if (country == null)
            responseDTO.notFound("language", parameter);
        else
            responseDTO.successfullyFetched(converter.toCountryDTO(country));

        return responseDTO;
    }
}