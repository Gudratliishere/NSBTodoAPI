package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.RegionDTO;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.entity.Region;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateRegionException;
import com.gudratli.nsbtodoapi.service.inter.RegionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/region")
public class RegionController
{
    private final RegionService regionService;
    private final ModelMapper modelMapper;

    @GetMapping(value = {"/getAll", ""})
    public ResponseEntity<ResponseDTO<List<RegionDTO>>> getAll ()
    {
        List<Region> regions = regionService.getAll();
        List<RegionDTO> regionDTOs = new ArrayList<>();

        regions.forEach(region -> regionDTOs.add(modelMapper.map(region, RegionDTO.class)));

        ResponseDTO<List<RegionDTO>> responseDTO = new ResponseDTO<>(regionDTOs);
        responseDTO.setSuccessMessage("Successfully fetched all data.");

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    public ResponseEntity<ResponseDTO<RegionDTO>> getById (@PathVariable Integer id)
    {
        System.out.println(org.hibernate.Version.getVersionString());
        Region region = regionService.getById(id);

        return ResponseEntity.ok(getResponseDTOWhenGetMapping(region, "id"));
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<ResponseDTO<RegionDTO>> getByName (@PathVariable String name)
    {
        Region region = regionService.getByName(name);

        return ResponseEntity.ok(getResponseDTOWhenGetMapping(region, "name"));
    }

    @GetMapping("/getByNameContaining/{name}")
    public ResponseEntity<ResponseDTO<List<RegionDTO>>> getByNameContaining (@PathVariable String name)
    {
        List<Region> regions = regionService.getByNameContaining(name);
        List<RegionDTO> regionDTOs = new ArrayList<>();

        regions.forEach(region -> regionDTOs.add(modelMapper.map(region, RegionDTO.class)));

        ResponseDTO<List<RegionDTO>> responseDTO = new ResponseDTO<>(regionDTOs);
        responseDTO.setSuccessMessage("Successfully fetched data with this name containing");

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<RegionDTO>> add (@RequestBody RegionDTO regionDTO)
    {
        Region region = modelMapper.map(regionDTO, Region.class);
        ResponseDTO<RegionDTO> responseDTO = new ResponseDTO<>(regionDTO);
        try
        {
            regionService.add(region);
            responseDTO.setSuccessMessage("Successfully added new Region.");
        } catch (DuplicateRegionException e)
        {
            responseDTO.setErrorCode(304);
            responseDTO.setErrorMessage(e.getMessage());
        }
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<RegionDTO>> update (@RequestBody RegionDTO regionDTO, @PathVariable Integer id)
    {
        Region region = regionService.getById(id);
        ResponseDTO<RegionDTO> responseDTO = new ResponseDTO<>(regionDTO);
        if (region == null)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("There is not any region with this id.");
            return ResponseEntity.ok(responseDTO);
        }
        region = modelMapper.map(regionDTO, Region.class);
        region.setId(id);
        try
        {
            regionService.update(region);
            responseDTO.setSuccessMessage("Successfully updated region.");
        } catch (DuplicateRegionException e)
        {
            responseDTO.setErrorCode(304);
            responseDTO.setErrorMessage(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<RegionDTO>> delete (@PathVariable Integer id)
    {
        Region region = regionService.getById(id);
        ResponseDTO<RegionDTO> responseDTO = new ResponseDTO<>();
        if (region == null)
        {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("There is not region with this id.");
            return ResponseEntity.ok(responseDTO);
        }

        regionService.remove(id);
        responseDTO.setObject(modelMapper.map(region, RegionDTO.class));
        responseDTO.setSuccessMessage("Successfully deleted region with id: " + id);
        return ResponseEntity.ok(responseDTO);
    }

    private ResponseDTO<RegionDTO> getResponseDTOWhenGetMapping (Region region, String parameter)
    {
        ResponseDTO<RegionDTO> responseDTO;
        if (region != null)
        {
            RegionDTO regionDTO = modelMapper.map(region, RegionDTO.class);
            responseDTO = new ResponseDTO<>(regionDTO);
            responseDTO.setSuccessMessage("Region successfully fetched.");
        } else
        {
            responseDTO = new ResponseDTO<>();
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("There is not any region with this " + parameter);
        }
        return responseDTO;
    }
}