package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.CountryDTO;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.entity.Country;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateCountryException;
import com.gudratli.nsbtodoapi.service.inter.CountryService;
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
@RequestMapping("/country")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
@Api("Country controller")
public class CountryController
{
    private final CountryService countryService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    @ApiOperation(value = "Get All", notes = "Returns all countries.")
    public ResponseEntity<ResponseDTO<List<CountryDTO>>> getAll ()
    {
        List<Country> countries = countryService.getAll();

        return ResponseEntity.ok(getResponseArray(countries));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    @ApiOperation(value = "Get by ID", notes = "Returns single Country according to ID.")
    public ResponseEntity<ResponseDTO<CountryDTO>> getById (@PathVariable @ApiParam(name = "ID",
            value = "ID of country", required = true, example = "16") Integer id)
    {
        Country country = countryService.getById(id);

        return ResponseEntity.ok(getResponseSingle(country, "id"));
    }

    @GetMapping("/getByName/{name}")
    @ApiOperation(value = "Get by name", notes = "Returns single Country according to name, because name is unique.")
    public ResponseEntity<ResponseDTO<CountryDTO>> getByName (@PathVariable @ApiParam(name = "name",
            value = "Name of the country", required = true, example = "Turkey") String name)
    {
        Country country = countryService.getByName(name);

        return ResponseEntity.ok(getResponseSingle(country, "name."));
    }

    @GetMapping("/getByNameContaining/{name}")
    @ApiOperation(value = "Get by name containing", notes = "Returns list of country that contains this key.")
    public ResponseEntity<ResponseDTO<List<CountryDTO>>> getByNameContaining (@PathVariable @ApiParam(name = "name",
            value = "Name that contains countries' name.", required = true, example = "Turk") String name)
    {
        List<Country> countries = countryService.getByNameContaining(name);

        return ResponseEntity.ok(getResponseArray(countries));
    }

    @GetMapping("/getByRegionId/{id}")
    @ApiOperation(value = "Get by region ID", notes = "Returns list of country that places certain region.")
    public ResponseEntity<ResponseDTO<List<CountryDTO>>> getByRegionId (@PathVariable @ApiParam(name = "ID",
            value = "ID of region", required = true, example = "22") Integer id)
    {
        List<Country> countries = countryService.getByRegionId(id);

        return ResponseEntity.ok(getResponseArray(countries));
    }

    @PostMapping
    @ApiOperation(value = "Add", notes = "Add new country.")
    public ResponseEntity<ResponseDTO<CountryDTO>> add (@Valid @RequestBody @ApiParam(name = "Country",
            value = "DTO for country objects.", required = true) CountryDTO countryDTO)
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

    @PutMapping
    @ApiOperation(value = "Update", notes = "Update exist country with it's ID.")
    public ResponseEntity<ResponseDTO<CountryDTO>> update (@Valid @RequestBody @ApiParam(name = "Country",
            value = "DTO for storing country.", required = true) CountryDTO countryDTO)
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
    @ApiOperation(value = "Delete", notes = "Delete country with it's ID.")
    public ResponseEntity<ResponseDTO<CountryDTO>> delete (@PathVariable @ApiParam(name = "ID",
            value = "ID of country", required = true, example = "12") Integer id)
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