package com.gudratli.nsbtodoapi.dto;

import com.gudratli.nsbtodoapi.entity.Process;
import com.gudratli.nsbtodoapi.entity.*;
import com.gudratli.nsbtodoapi.service.inter.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Converter
{
    private final ModelMapper modelMapper;
    private final RegionService regionService;
    private final ProcessService processService;
    private final UserService userService;
    private final TaskService taskService;
    private final StatusService statusService;
    private final CountryService countryService;
    private final LanguageService languageService;
    private final TechnologyService technologyService;

    //Country converter
    public CountryDTO toCountryDTO (Country country)
    {
        CountryDTO countryDTO = modelMapper.map(country, CountryDTO.class);
        countryDTO.setRegionId(country.getRegion().getId());
        return countryDTO;
    }

    public Country toCountry (CountryDTO countryDTO)
    {
        Country country = modelMapper.map(countryDTO, Country.class);
        country.setRegion(regionService.getById(countryDTO.getRegionId()));
        return country;
    }

    public void toCountry (Country country, CountryDTO countryDTO)
    {
        modelMapper.map(countryDTO, country);
        country.setRegion(regionService.getById(countryDTO.getRegionId()));
    }
    //Country ends

    //Conversation converter
    public ConversationDTO toConversationDTO (Conversation conversation)
    {
        ConversationDTO conversationDTO = modelMapper.map(conversation, ConversationDTO.class);
        conversationDTO.setProcessId(conversation.getProcess().getId());
        conversationDTO.setUserId(conversation.getUser().getId());

        return conversationDTO;
    }

    public Conversation toConversation (ConversationDTO conversationDTO)
    {
        Conversation conversation = modelMapper.map(conversationDTO, Conversation.class);
        conversation.setProcess(processService.getById(conversationDTO.getProcessId()));
        conversation.setUser(userService.getById(conversationDTO.getUserId()));

        return conversation;
    }

    public void toConversation (Conversation conversation, ConversationDTO conversationDTO)
    {
        modelMapper.map(conversationDTO, conversation);
        conversation.setProcess(processService.getById(conversationDTO.getProcessId()));
        conversation.setUser(userService.getById(conversationDTO.getUserId()));
    }
    //Conversation ends

    //EmailToken
    public EmailTokenDTO toEmailTokenDTO (EmailToken emailToken)
    {
        return modelMapper.map(emailToken, EmailTokenDTO.class);
    }

    public EmailToken toEmailToken (EmailTokenDTO emailTokenDTO)
    {
        return modelMapper.map(emailTokenDTO, EmailToken.class);
    }

    public void toEmailToken (EmailToken emailToken, EmailTokenDTO emailTokenDTO)
    {
        modelMapper.map(emailTokenDTO, emailToken);
    }
    //EmailToken ends

    //Language
    public LanguageDTO toLanguageDTO (Language language)
    {
        return modelMapper.map(language, LanguageDTO.class);
    }

    public Language toLanguage (LanguageDTO languageDTO)
    {
        return modelMapper.map(languageDTO, Language.class);
    }

    public void toLanguage (Language language, LanguageDTO languageDTO)
    {
        modelMapper.map(languageDTO, language);
    }
    //Language ends

    //Process
    public ProcessDTO toProcessDTO (Process process)
    {
        ProcessDTO processDTO = modelMapper.map(process, ProcessDTO.class);
        processDTO.setUserId(process.getUser().getId());
        processDTO.setTaskId(process.getTask().getId());
        processDTO.setStatusId(process.getStatus().getId());

        return processDTO;
    }

    public Process toProcess (ProcessDTO processDTO)
    {
        Process process = modelMapper.map(processDTO, Process.class);
        process.setUser(userService.getById(processDTO.getUserId()));
        process.setTask(taskService.getById(processDTO.getTaskId()));
        process.setStatus(statusService.getById(processDTO.getStatusId()));

        return process;
    }

    public void toProcess (Process process, ProcessDTO processDTO)
    {
        modelMapper.map(processDTO, process);
        process.setUser(userService.getById(processDTO.getUserId()));
        process.setTask(taskService.getById(processDTO.getTaskId()));
        process.setStatus(statusService.getById(processDTO.getStatusId()));
    }
    //Process ends

    //Region converter
    public RegionDTO toRegionDTO (Region region)
    {
        return modelMapper.map(region, RegionDTO.class);
    }

    public Region toRegion (RegionDTO regionDTO)
    {
        return modelMapper.map(regionDTO, Region.class);
    }

    public void toRegion (Region region, RegionDTO regionDTO)
    {
        modelMapper.map(regionDTO, region);
    }
    //Region ends

    //Role
    public RoleDTO toRoleDTO (Role role)
    {
        return modelMapper.map(role, RoleDTO.class);
    }

    public Role toRole (RoleDTO roleDTO)
    {
        return modelMapper.map(roleDTO, Role.class);
    }

    public void toRole (Role role, RoleDTO roleDTO)
    {
        modelMapper.map(roleDTO, role);
    }
    //Role ends

    //Status
    public StatusDTO toStatusDTO (Status status)
    {
        return modelMapper.map(status, StatusDTO.class);
    }

    public Status toStatus (StatusDTO statusDTO)
    {
        return modelMapper.map(statusDTO, Status.class);
    }

    public void toStatus (Status status, StatusDTO statusDTO)
    {
        modelMapper.map(statusDTO, status);
    }
    //Status ends

    //Task
    public TaskDTO toTaskDTO (Task task)
    {
        return modelMapper.map(task, TaskDTO.class);
    }

    public Task toTask (TaskDTO taskDTO)
    {
        return modelMapper.map(taskDTO, Task.class);
    }

    public void toTask (Task task, TaskDTO taskDTO)
    {
        modelMapper.map(taskDTO, task);
    }
    //Task ends

    //Technology
    public TechnologyDTO toTechnologyDTO (Technology technology)
    {
        return modelMapper.map(technology, TechnologyDTO.class);
    }

    public Technology toTechnology (TechnologyDTO technologyDTO)
    {
        return modelMapper.map(technologyDTO, Technology.class);
    }

    public void toTechnology (Technology technology, TechnologyDTO technologyDTO)
    {
        modelMapper.map(technologyDTO, technology);
    }
    //Technology ends

    //User
    public UserDTO toUserDTO (User user)
    {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setCountryId(user.getCountry().getId());

        return userDTO;
    }

    public User toUser (UserDTO userDTO)
    {
        User user = modelMapper.map(userDTO, User.class);
        user.setCountry(countryService.getById(userDTO.getCountryId()));

        return user;
    }

    public void toUser (User user, UserDTO userDTO)
    {
        modelMapper.map(userDTO, user);
        user.setCountry(countryService.getById(userDTO.getCountryId()));
    }
    //User ends

    //UserLanguage
    public UserLanguageDTO toUserLanguageDTO (UserLanguage userLanguage)
    {
        UserLanguageDTO userLanguageDTO = modelMapper.map(userLanguage, UserLanguageDTO.class);
        userLanguageDTO.setLanguageId(userLanguage.getLanguage().getId());
        userLanguageDTO.setUserId(userLanguage.getUser().getId());

        return userLanguageDTO;
    }

    public UserLanguage toUserLanguage (UserLanguageDTO userLanguageDTO)
    {
        UserLanguage userLanguage = modelMapper.map(userLanguageDTO, UserLanguage.class);
        userLanguage.setLanguage(languageService.getById(userLanguageDTO.getLanguageId()));
        userLanguage.setUser(userService.getById(userLanguageDTO.getUserId()));

        return userLanguage;
    }

    public void toUserLanguage (UserLanguage userLanguage, UserLanguageDTO userLanguageDTO)
    {
        modelMapper.map(userLanguageDTO, userLanguage);
        userLanguage.setLanguage(languageService.getById(userLanguageDTO.getLanguageId()));
        userLanguage.setUser(userService.getById(userLanguageDTO.getUserId()));
    }
    //UserLanguage ends

    //UserTechnology
    public UserTechnologyDTO toUserTechnologyDTO (UserTechnology userTechnology)
    {
        UserTechnologyDTO userTechnologyDTO = modelMapper.map(userTechnology, UserTechnologyDTO.class);
        userTechnologyDTO.setTechnologyId(userTechnology.getTechnology().getId());
        userTechnologyDTO.setUserId(userTechnology.getUser().getId());

        return userTechnologyDTO;
    }

    public UserTechnology toUserTechnology (UserTechnologyDTO userTechnologyDTO)
    {
        UserTechnology userTechnology = modelMapper.map(userTechnologyDTO, UserTechnology.class);
        userTechnology.setTechnology(technologyService.getById(userTechnologyDTO.getTechnologyId()));
        userTechnology.setUser(userService.getById(userTechnologyDTO.getUserId()));

        return userTechnology;
    }

    public void toUserTechnology (UserTechnology userTechnology, UserTechnologyDTO userTechnologyDTO)
    {
        modelMapper.map(userTechnologyDTO, userTechnology);
        userTechnology.setTechnology(technologyService.getById(userTechnologyDTO.getTechnologyId()));
        userTechnology.setUser(userService.getById(userTechnologyDTO.getUserId()));
    }
    //UserTechnology ends
}
