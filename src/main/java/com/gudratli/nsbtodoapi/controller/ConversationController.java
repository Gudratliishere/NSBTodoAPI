package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.ConversationDTO;
import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.entity.Conversation;
import com.gudratli.nsbtodoapi.service.inter.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/conversation")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
public class ConversationController
{
    private final ConversationService conversationService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    public ResponseEntity<ResponseDTO<List<ConversationDTO>>> getAll ()
    {
        List<Conversation> conversations = conversationService.getAll();

        return ResponseEntity.ok(getResponseWithList(conversations));
    }

    @GetMapping("/getByUserId/{id}")
    public ResponseEntity<ResponseDTO<List<ConversationDTO>>> getByUserId (@PathVariable Integer id)
    {
        List<Conversation> conversations = conversationService.getByUserId(id);

        return ResponseEntity.ok(getResponseWithList(conversations));
    }

    @GetMapping("/getByProcessId/{id}")
    public ResponseEntity<ResponseDTO<List<ConversationDTO>>> getByProcessId (@PathVariable Integer id)
    {
        List<Conversation> conversations = conversationService.getByProcessId(id);

        return ResponseEntity.ok(getResponseWithList(conversations));
    }

    @GetMapping("/getByUserIdAndProcessId/{userId}/{processId}")
    public ResponseEntity<ResponseDTO<List<ConversationDTO>>> getByUserIdAndProcessId (@PathVariable Integer userId,
            @PathVariable Integer processId)
    {
        List<Conversation> conversations = conversationService.getByUserIdAndProcessId(userId, processId);

        return ResponseEntity.ok(getResponseWithList(conversations));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    public ResponseEntity<ResponseDTO<ConversationDTO>> getById (@PathVariable Integer id)
    {
        Conversation conversation = conversationService.getById(id);
        ResponseDTO<ConversationDTO> responseDTO = new ResponseDTO<>();

        if (conversation == null)
            return ResponseEntity.ok(responseDTO.notFound("conversation", "id."));

        return ResponseEntity.ok(responseDTO.successfullyFetched(converter.toConversationDTO(conversation)));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<ConversationDTO>> add (@RequestBody ConversationDTO conversationDTO)
    {
        ResponseDTO<ConversationDTO> responseDTO = new ResponseDTO<>(conversationDTO);
        Conversation conversation = converter.toConversation(conversationDTO);
        conversation.setId(null);

        conversation = conversationService.add(conversation);

        return ResponseEntity.ok(responseDTO.successfullyInserted(converter.toConversationDTO(conversation)));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<ConversationDTO>> update (@RequestBody ConversationDTO conversationDTO)
    {
        ResponseDTO<ConversationDTO> responseDTO = new ResponseDTO<>();
        Conversation conversation = conversationService.getById(conversationDTO.getId());

        if (conversation == null)
            return ResponseEntity.ok(responseDTO.notFound("conversation", "id."));

        converter.toConversation(conversation, conversationDTO);

        conversation = conversationService.update(conversation);

        return ResponseEntity.ok(responseDTO.successfullyUpdated(converter.toConversationDTO(conversation)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<ConversationDTO>> delete (@PathVariable Integer id)
    {
        ResponseDTO<ConversationDTO> responseDTO = new ResponseDTO<>();
        Conversation conversation = conversationService.getById(id);

        if (conversation == null)
            return ResponseEntity.ok(responseDTO.notFound("conversation", "id."));

        conversationService.remove(id);

        return ResponseEntity.ok(responseDTO.successfullyDeleted(converter.toConversationDTO(conversation)));
    }

    private ResponseDTO<List<ConversationDTO>> getResponseWithList (List<Conversation> conversations)
    {
        List<ConversationDTO> conversationDTOs = new ArrayList<>();

        conversations.forEach(conversation -> conversationDTOs.add(converter.toConversationDTO(conversation)));

        ResponseDTO<List<ConversationDTO>> responseDTO = new ResponseDTO<>();
        return responseDTO.successfullyFetched(conversationDTOs);
    }
}