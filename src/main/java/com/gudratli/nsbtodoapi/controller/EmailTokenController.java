package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.EmailTokenDTO;
import com.gudratli.nsbtodoapi.dto.EmailTokenVerifyDTO;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.entity.EmailToken;
import com.gudratli.nsbtodoapi.service.inter.EmailTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emailToken")
@Validated
@Api("Email token")
public class EmailTokenController
{
    private final EmailTokenService emailTokenService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Get All", notes = "Returns all tokens.")
    public ResponseEntity<ResponseDTO<List<EmailTokenDTO>>> getAll ()
    {
        List<EmailToken> emailTokens = emailTokenService.getAll();

        return ResponseEntity.ok(getResponseWithList(emailTokens));
    }

    @GetMapping("/getByEmail/{email}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Get by email", notes = "Returns list of tokens according to email.")
    public ResponseEntity<ResponseDTO<List<EmailTokenDTO>>> getByEmail (@PathVariable @ApiParam(name = "Email",
            value = "Email that you want to see it's tokens.", required = true, example = "dunay@gmail.com") String email)
    {
        List<EmailToken> emailTokens = emailTokenService.getByEmail(email);

        return ResponseEntity.ok(getResponseWithList(emailTokens));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Get by ID", notes = "Returns single email token according to ID.")
    public ResponseEntity<ResponseDTO<EmailTokenDTO>> getById (@PathVariable @ApiParam(name = "ID",
            value = "ID of email token", required = true, example = "18") Integer id)
    {
        EmailToken emailToken = emailTokenService.getById(id);

        return ResponseEntity.ok(getResponse(emailToken, "id."));
    }

    @GetMapping("/getActiveByEmail/{email}")
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Get active by email",
            notes = "Returns single email token that holds active token according to email.")
    public ResponseEntity<ResponseDTO<EmailTokenDTO>> getActiveByEmail (@PathVariable @ApiParam(name = "Email",
            value = "Email that you want to see it's active token.", required = true, example = "dunay@gmail.com") String email)
    {
        EmailToken emailToken = emailTokenService.getActiveByEmail(email);

        return ResponseEntity.ok(getResponse(emailToken, "email."));
    }

    @PostMapping("/isValid")
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Is valid?",
            notes = "Returns true or false that indicates email token is valid or not or expired.")
    public ResponseEntity<ResponseDTO<Boolean>> isValid (@Valid @RequestBody @ApiParam(name = "Email Token Verify",
            value = "DTO for verifying email token.", required = true) EmailTokenVerifyDTO emailTokenVerifyDTO)
    {
        EmailToken emailToken = emailTokenService.getById(emailTokenVerifyDTO.getId());
        ResponseDTO<Boolean> responseDTO = new ResponseDTO<>();

        if (emailToken == null)
            return ResponseEntity.ok(responseDTO.notFound("emailToken", "id."));

        if (!emailToken.getToken().equals(emailTokenVerifyDTO.getToken()))
        {
            responseDTO.setObject(false);
            responseDTO.setSuccessMessage("Token is not valid.");
        } else if (emailToken.getStatus() && !emailTokenService.isExpired(emailToken))
        {
            responseDTO.setObject(true);
            responseDTO.setSuccessMessage("Token is valid.");
        } else
        {
            responseDTO.setObject(false);
            responseDTO.setSuccessMessage("Token is expired.");
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/generateToken/{email}")
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Generate token",
            notes = "Generates new token for that email and returns DTO that holds it's ID.")
    public ResponseEntity<ResponseDTO<EmailTokenDTO>> generateToken (
            @PathVariable @Email(message = "Email must be valid") @ApiParam(name = "Email",
                    value = "Email that you want to generate new token for it.", required = true, example = "dunay@gmail.com")
                    String email)
    {
        EmailToken emailToken = emailTokenService.generateToken(email);

        ResponseDTO<EmailTokenDTO> responseDTO = new ResponseDTO<>(converter.toEmailTokenDTO(emailToken));
        responseDTO.setSuccessMessage("Successfully generated token.");

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/expire/{id}")
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Expire",
            notes = "Expire email token with it's ID, so it indicates that email token has been used.")
    public ResponseEntity<ResponseDTO<EmailTokenDTO>> expire (@PathVariable @ApiParam(name = "ID",
            value = "ID of email token you want to expire.", required = true, example = "16") Integer id)
    {
        EmailToken emailToken = emailTokenService.getById(id);
        ResponseDTO<EmailTokenDTO> responseDTO = new ResponseDTO<>();

        if (emailToken == null)
            return ResponseEntity.ok(responseDTO.notFound("emailToken", "id."));

        emailTokenService.expireEmailToken(id);
        responseDTO.setObject(converter.toEmailTokenDTO(emailToken));
        responseDTO.setSuccessMessage("Successfully expired this token.");

        return ResponseEntity.ok(responseDTO);
    }

    private ResponseDTO<List<EmailTokenDTO>> getResponseWithList (List<EmailToken> emailTokens)
    {
        List<EmailTokenDTO> emailTokenDTOs = new ArrayList<>();

        emailTokens.forEach(emailToken -> emailTokenDTOs.add(converter.toEmailTokenDTO(emailToken)));

        ResponseDTO<List<EmailTokenDTO>> responseDTO = new ResponseDTO<>();
        return responseDTO.successfullyFetched(emailTokenDTOs);
    }

    private ResponseDTO<EmailTokenDTO> getResponse (EmailToken emailToken, String parameter)
    {
        ResponseDTO<EmailTokenDTO> responseDTO = new ResponseDTO<>();

        if (emailToken == null)
            return responseDTO.notFound("emailToken", parameter);

        EmailTokenDTO emailTokenDTO = converter.toEmailTokenDTO(emailToken);
        return responseDTO.successfullyFetched(emailTokenDTO);
    }
}