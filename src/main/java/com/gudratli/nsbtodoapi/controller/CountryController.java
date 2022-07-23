package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.CountryDTO;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.entity.Country;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateCountryException;
import com.gudratli.nsbtodoapi.service.inter.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/country")
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
    public ResponseEntity<ResponseDTO<CountryDTO>> add (@RequestBody CountryDTO countryDTO)
    {
        Country country = converter.toCountry(countryDTO);
        country.setId(null);

        ResponseDTO<CountryDTO> responseDTO = new ResponseDTO<>();

        try
        {
            country = countryService.add(country);
            responseDTO.setObject(converter.toCountryDTO(country));
            responseDTO.setSuccessMessage("Successfully inserted.");
        } catch (DuplicateCountryException e)
        {
            responseDTO.setErrorCode(304);
            responseDTO.setErrorMessage(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping()
    public ResponseEntity<ResponseDTO<CountryDTO>> update (@RequestBody CountryDTO countryDTO)
    {
        Country country = converter.toCountry(countryDTO);
        ResponseDTO<CountryDTO> responseDTO = new ResponseDTO<>();

        if (country == null)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("There is not country with this id.");
            return ResponseEntity.ok(responseDTO);
        }

        try
        {
            country = countryService.update(country);
            responseDTO.setObject(converter.toCountryDTO(country));
            responseDTO.setSuccessMessage("Successfully updated.");
        } catch (DuplicateCountryException e)
        {
            responseDTO.setErrorCode(304);
            responseDTO.setErrorMessage(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<CountryDTO>> delete (@PathVariable Integer id)
    {
        Country country = countryService.getById(id);
        ResponseDTO<CountryDTO> responseDTO = new ResponseDTO<>();

        if (country == null)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("There is not country with this id.");
        } else
        {
            countryService.remove(id);
            responseDTO.setObject(converter.toCountryDTO(country));
            responseDTO.setSuccessMessage("Successfully deleted country.");
        }

        return ResponseEntity.ok(responseDTO);
    }

    private ResponseDTO<List<CountryDTO>> getResponseArray (List<Country> countries)
    {
        List<CountryDTO> countryDTOs = new ArrayList<>();

        countries.forEach(country -> countryDTOs.add(converter.toCountryDTO(country)));

        ResponseDTO<List<CountryDTO>> responseDTO = new ResponseDTO<>(countryDTOs);
        responseDTO.setSuccessMessage("Successfully fetched all data.");

        return responseDTO;
    }

    private ResponseDTO<CountryDTO> getResponseSingle (Country country, String parameter)
    {
        ResponseDTO<CountryDTO> responseDTO = new ResponseDTO<>();
        if (country == null)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("There is not country with this " + parameter);
        } else
        {
            CountryDTO countryDTO = converter.toCountryDTO(country);
            responseDTO.setObject(countryDTO);
            responseDTO.setSuccessMessage("Successfully fetched country.");
        }

        return responseDTO;
    }
}
