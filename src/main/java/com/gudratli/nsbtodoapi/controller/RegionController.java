package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.RegionDTO;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.entity.Region;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateRegionException;
import com.gudratli.nsbtodoapi.service.inter.RegionService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/region")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
@Api("Region controller")
public class RegionController
{
    private final RegionService regionService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    @ApiOperation(value = "Get All", notes = "Returns all regions.")
    public ResponseEntity<ResponseDTO<List<RegionDTO>>> getAll ()
    {
        List<Region> regions = regionService.getAll();

        return ResponseEntity.ok(getResponseWithList(regions));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    @ApiOperation(value = "Get by ID", notes = "Returns single region according to it's ID.")
    public ResponseEntity<ResponseDTO<RegionDTO>> getById (@PathVariable @ApiParam(name = "ID",
            value = "ID of the region.", required = true, example = "12") Integer id)
    {
        System.out.println(org.hibernate.Version.getVersionString());
        Region region = regionService.getById(id);

        return ResponseEntity.ok(getResponseDTOWhenGetMapping(region, "id"));
    }

    @GetMapping("/getByName/{name}")
    @ApiOperation(value = "Get by name", notes = "Get single region according to it's name.")
    public ResponseEntity<ResponseDTO<RegionDTO>> getByName (@PathVariable @ApiParam(name = "Name",
            value = "Name of the region", required = true, example = "Europa") String name)
    {
        Region region = regionService.getByName(name);

        return ResponseEntity.ok(getResponseDTOWhenGetMapping(region, "name"));
    }

    @GetMapping("/getByNameContaining/{name}")
    @ApiOperation(value = "Get by name containing", notes = "Get list of regions that contains that key")
    public ResponseEntity<ResponseDTO<List<RegionDTO>>> getByNameContaining (@PathVariable @ApiParam(name = "Name",
            value = "Name that you think regions contain.", required = true, example = "Eur") String name)
    {
        List<Region> regions = regionService.getByNameContaining(name);

        return ResponseEntity.ok(getResponseWithList(regions));
    }

    @PostMapping
    @ApiOperation(value = "Add", notes = "Add new region.")
    public ResponseEntity<ResponseDTO<RegionDTO>> add (@Valid @RequestBody @ApiParam(name = "Region",
            value = "DTO for region", required = true) RegionDTO regionDTO)
    {
        Region region = converter.toRegion(regionDTO);
        region.setId(null);

        ResponseDTO<RegionDTO> responseDTO = new ResponseDTO<>();
        try
        {
            regionService.add(region);
            responseDTO.successfullyInserted(converter.toRegionDTO(region));
        } catch (DuplicateRegionException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping()
    @ApiOperation(value = "Update", notes = "Update existing region.")
    public ResponseEntity<ResponseDTO<RegionDTO>> update (@Valid @RequestBody @ApiParam(name = "Region",
            value = "DTO for region", required = true) RegionDTO regionDTO)
    {
        Region region = regionService.getById(regionDTO.getId());
        ResponseDTO<RegionDTO> responseDTO = new ResponseDTO<>(regionDTO);

        if (region == null)
            return ResponseEntity.ok(responseDTO.notFound("region", "id."));

        converter.toRegion(region, regionDTO);

        try
        {
            region = regionService.update(region);
            responseDTO.successfullyUpdated(converter.toRegionDTO(region));
        } catch (DuplicateRegionException e)
        {
            responseDTO.duplicateException(e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete", notes = "Delete single region according to it's ID.")
    public ResponseEntity<ResponseDTO<RegionDTO>> delete (@PathVariable @ApiParam(name = "ID",
            value = "ID of the region", required = true, example = "5") Integer id)
    {
        Region region = regionService.getById(id);
        ResponseDTO<RegionDTO> responseDTO = new ResponseDTO<>();

        if (region == null)
            return ResponseEntity.ok(responseDTO.notFound("language", "id."));

        regionService.remove(id);
        responseDTO.successfullyDeleted(converter.toRegionDTO(region));

        return ResponseEntity.ok(responseDTO);
    }

    private ResponseDTO<List<RegionDTO>> getResponseWithList (List<Region> regions)
    {
        List<RegionDTO> regionDTOs = new ArrayList<>();

        regions.forEach(region -> regionDTOs.add(converter.toRegionDTO(region)));

        ResponseDTO<List<RegionDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.successfullyFetched(regionDTOs);

        return responseDTO;
    }

    private ResponseDTO<RegionDTO> getResponseDTOWhenGetMapping (Region region, String parameter)
    {
        ResponseDTO<RegionDTO> responseDTO = new ResponseDTO<>();

        if (region != null)
            responseDTO.successfullyFetched(converter.toRegionDTO(region));
        else
            responseDTO.notFound("region", parameter);

        return responseDTO;
    }
}