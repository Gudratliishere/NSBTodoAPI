package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.CountryDTO;
import com.gudratli.nsbtodoapi.dto.RegionDTO;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.entity.Country;
import com.gudratli.nsbtodoapi.service.inter.CountryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/country")
public class CountryController
{
    private final CountryService countryService;
    private final ModelMapper modelMapper;

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    public ResponseEntity<ResponseDTO<CountryDTO>> getById (@PathVariable Integer id)
    {
        Country country = countryService.getById(id);
        ResponseDTO<CountryDTO> responseDTO = new ResponseDTO<>();
        if (country == null)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("There is not country with this id.");
        }
        else
        {
            CountryDTO countryDTO = modelMapper.map(country, CountryDTO.class);
            countryDTO.setRegionDTO(modelMapper.map(country.getRegion(), RegionDTO.class));
            responseDTO.setObject(countryDTO);
            responseDTO.setSuccessMessage("Successfully fetched country.");
        }
        return ResponseEntity.ok(responseDTO);
    }
}
