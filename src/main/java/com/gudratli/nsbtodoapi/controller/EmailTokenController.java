package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.EmailTokenDTO;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.entity.EmailToken;
import com.gudratli.nsbtodoapi.service.inter.EmailTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emailToken")
public class EmailTokenController
{
    private final EmailTokenService emailTokenService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    public ResponseEntity<ResponseDTO<List<EmailTokenDTO>>> getAll ()
    {
        List<EmailToken> emailTokens = emailTokenService.getAll();

        return ResponseEntity.ok(getResponseWithList(emailTokens));
    }

    @GetMapping("/getByEmail/{email}")
    public ResponseEntity<ResponseDTO<List<EmailTokenDTO>>> getByEmail (@PathVariable String email)
    {
        List<EmailToken> emailTokens = emailTokenService.getByEmail(email);

        return ResponseEntity.ok(getResponseWithList(emailTokens));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    public ResponseEntity<ResponseDTO<EmailTokenDTO>> getById (@PathVariable Integer id)
    {
        EmailToken emailToken = emailTokenService.getById(id);

        return ResponseEntity.ok(getResponse(emailToken, "id."));
    }

    @GetMapping("/getActiveByEmail/{email}")
    public ResponseEntity<ResponseDTO<EmailTokenDTO>> getActiveByEmail (@PathVariable String email)
    {
        EmailToken emailToken = emailTokenService.getActiveByEmail(email);

        return ResponseEntity.ok(getResponse(emailToken, "email."));
    }

    @GetMapping("/isExpired/{id}")
    public ResponseEntity<ResponseDTO<Boolean>> isExpired (@PathVariable Integer id)
    {
        EmailToken emailToken = emailTokenService.getById(id);
        ResponseDTO<Boolean> responseDTO = new ResponseDTO<>();

        if (emailToken == null)
            return ResponseEntity.ok(responseDTO.notFound("emailToken", "id."));

        if (emailToken.getStatus() && !emailTokenService.isExpired(emailToken))
        {
            responseDTO.setObject(false);
            responseDTO.setSuccessMessage("Token is valid.");
        } else
        {
            responseDTO.setObject(true);
            responseDTO.setSuccessMessage("Token is expired.");
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/{email}")
    public ResponseEntity<ResponseDTO<EmailTokenDTO>> generateToken (@PathVariable String email)
    {
        EmailToken emailToken = emailTokenService.generateToken(email);

        ResponseDTO<EmailTokenDTO> responseDTO = new ResponseDTO<>(converter.toEmailTokenDTO(emailToken));
        responseDTO.setSuccessMessage("Successfully generated token.");

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/expireEmailToken/{id}")
    public ResponseEntity<ResponseDTO<EmailTokenDTO>> expire (@PathVariable Integer id)
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