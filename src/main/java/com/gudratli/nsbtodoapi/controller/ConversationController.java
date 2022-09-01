package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.ConversationDTO;
import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.entity.Conversation;
import com.gudratli.nsbtodoapi.service.inter.ConversationService;
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
@RequestMapping("/conversation")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
@Api(value = "Conversation controller")
public class ConversationController
{
    private final ConversationService conversationService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    @ApiOperation(value = "Get All", notes = "Returns all the conversations.")
    public ResponseEntity<ResponseDTO<List<ConversationDTO>>> getAll ()
    {
        List<Conversation> conversations = conversationService.getAll();

        return ResponseEntity.ok(getResponseWithList(conversations));
    }

    @GetMapping("/getByUserId/{id}")
    @ApiOperation(value = "Get by user ID", notes = "Get all conversations of certain user.")
    public ResponseEntity<ResponseDTO<List<ConversationDTO>>> getByUserId (@PathVariable @ApiParam(name = "ID",
            value = "ID of user.", required = true, example = "12") Integer id)
    {
        List<Conversation> conversations = conversationService.getByUserId(id);

        return ResponseEntity.ok(getResponseWithList(conversations));
    }

    @GetMapping("/getByProcessId/{id}")
    @ApiOperation(value = "Get by process ID", notes = "Get all conversations under certain process.")
    public ResponseEntity<ResponseDTO<List<ConversationDTO>>> getByProcessId (@PathVariable @ApiParam(name = "ID",
            value = "ID of process.", required = true, example = "15") Integer id)
    {
        List<Conversation> conversations = conversationService.getByProcessId(id);

        return ResponseEntity.ok(getResponseWithList(conversations));
    }

    @GetMapping("/getByUserIdAndProcessId/{userId}/{processId}")
    @ApiOperation(value = "Get by user ID and process ID",
            notes = "Get all conversation under certain process written by certain user")
    public ResponseEntity<ResponseDTO<List<ConversationDTO>>> getByUserIdAndProcessId (
            @PathVariable
            @ApiParam(name = "User ID", value = "ID of user", required = true, example = "15") Integer userId,
            @PathVariable
            @ApiParam(name = "Process ID", value = "ID of process", required = true, example = "12") Integer processId)
    {
        List<Conversation> conversations = conversationService.getByUserIdAndProcessId(userId, processId);

        return ResponseEntity.ok(getResponseWithList(conversations));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    @ApiOperation(value = "Get by ID", notes = "Get single conversation with ID.")
    public ResponseEntity<ResponseDTO<ConversationDTO>> getById (@PathVariable @ApiParam(name = "ID",
            value = "ID of conversation", required = true, example = "19") Integer id)
    {
        Conversation conversation = conversationService.getById(id);
        ResponseDTO<ConversationDTO> responseDTO = new ResponseDTO<>();

        if (conversation == null)
            return ResponseEntity.ok(responseDTO.notFound("conversation", "id."));

        return ResponseEntity.ok(responseDTO.successfullyFetched(converter.toConversationDTO(conversation)));
    }

    @PostMapping
    @ApiOperation(value = "Add", notes = "Add new conversation.")
    public ResponseEntity<ResponseDTO<ConversationDTO>> add (@Valid @RequestBody @ApiParam(name = "Conversation",
            value = "Conversation DTO", required = true) ConversationDTO conversationDTO)
    {
        ResponseDTO<ConversationDTO> responseDTO = new ResponseDTO<>(conversationDTO);
        Conversation conversation = converter.toConversation(conversationDTO);
        conversation.setId(null);

        conversation = conversationService.add(conversation);

        return ResponseEntity.ok(responseDTO.successfullyInserted(converter.toConversationDTO(conversation)));
    }

    @PutMapping
    @ApiOperation(value = "Update", notes = "Update certain conversation.")
    public ResponseEntity<ResponseDTO<ConversationDTO>> update (@Valid @RequestBody @ApiParam(name = "Conversation",
            value = "Conversation DTO", required = true) ConversationDTO conversationDTO)
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
    @ApiOperation(value = "Delete", notes = "Delete conversation with that id.")
    public ResponseEntity<ResponseDTO<ConversationDTO>> delete (@PathVariable @ApiParam(name = "ID",
            value = "ID of conversation", required = true, example = "15") Integer id)
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