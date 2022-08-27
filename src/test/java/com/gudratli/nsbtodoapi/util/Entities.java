package com.gudratli.nsbtodoapi.util;

import com.gudratli.nsbtodoapi.entity.Process;
import com.gudratli.nsbtodoapi.entity.*;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Entities
{
    //Region
    public static Region getRegion ()
    {
        Region region = new Region("Asia");
        region.setId(1);
        return region;
    }

    public static Region getRegion (String name, int id)
    {
        Region region = new Region(name);
        region.setId(id);
        return region;
    }

    public static List<Region> getRegionList ()
    {
        return Arrays.asList(getRegion("Asia", 1), getRegion("Europa", 2));
    }
    //Region end

    //Country
    public static Country getCountry (String name)
    {
        Country country = new Country(name, getRegion());
        country.setId(1);
        return country;
    }

    public static Country getCountry ()
    {
        Country country = new Country("UK", getRegion());
        country.setId(1);
        return country;
    }

    public static Country getCountry (String name, int id)
    {
        Country country = new Country(name, getRegion());
        country.setId(id);
        return country;
    }

    public static List<Country> getCountryList ()
    {
        return Arrays.asList(getCountry("UK", 1), getCountry("Russia", 2));
    }
    //Country end

    //File
    public static File getFile ()
    {
        File file = new File("file", "image/jpeg", 1234);
        file.setId(1);
        return file;
    }

    //Language
    public static Language getLanguage ()
    {
        Language language = new Language("Turkish");
        language.setId(1);
        return language;
    }

    public static Language getLanguage (String name, int id)
    {
        Language language = new Language(name);
        language.setId(id);
        return language;
    }

    public static List<Language> getLanguageList ()
    {
        return Arrays.asList(getLanguage("Turkish", 1), getLanguage("English", 2));
    }
    //Language end

    //Technology
    public static Technology getTechnology ()
    {
        Technology technology = new Technology("HTML");
        technology.setId(1);
        return technology;
    }

    public static Technology getTechnology (String name, int id)
    {
        Technology technology = new Technology(name);
        technology.setId(id);
        return technology;
    }

    public static List<Technology> getTechnologyList ()
    {
        return Arrays.asList(getTechnology(), getTechnology("CSS", 2));
    }
    //Technology

    //UserLanguage
    public static UserLanguage getUserLanguage ()
    {
        UserLanguage userLanguage = new UserLanguage(getUser(), getLanguage(), 8);
        userLanguage.setId(1);
        return userLanguage;
    }

    public static UserLanguage getUserLanguage (Integer level, int id)
    {
        UserLanguage userLanguage = new UserLanguage(getUser(), getLanguage(), level);
        userLanguage.setId(id);
        return userLanguage;
    }

    public static List<UserLanguage> getUserLanguageList ()
    {
        return Arrays.asList(getUserLanguage(), getUserLanguage(10, 2));
    }
    //UserLanguage end

    //UserTechnology
    public static UserTechnology getUserTechnology ()
    {
        UserTechnology userTechnology = new UserTechnology(getUser(), getTechnology(), 5);
        userTechnology.setId(1);
        return userTechnology;
    }

    public static UserTechnology getUserTechnology (Integer level, int id)
    {
        UserTechnology userTechnology = new UserTechnology(getUser(), getTechnology(), level);
        userTechnology.setId(id);
        return userTechnology;
    }

    public static List<UserTechnology> getUserTechnologyList ()
    {
        return Arrays.asList(getUserTechnology(), getUserTechnology(7, 2));
    }
    //UserTechnology end

    //Task
    public static Task getTask ()
    {
        Task task = new Task("Rename project", "You will rename project", getFile(), getFile());
        task.setId(1);
        return task;
    }

    public static Task getTask (String name, int id)
    {
        Task task = new Task(name, "You will rename project", getFile(), getFile());
        task.setId(id);
        return task;
    }

    public static List<Task> getTaskList ()
    {
        return Arrays.asList(getTask(), getTask("Delete equals", 2));
    }
    //Task end

    //Status
    public static Status getStatus ()
    {
        Status status = new Status("Finished");
        status.setId(1);
        return status;
    }

    public static Status getStatus (String name, int id)
    {
        Status status = new Status(name);
        status.setId(id);
        return status;
    }

    public static List<Status> getStatusList ()
    {
        return Arrays.asList(getStatus(), getStatus("Incomplete", 2));
    }
    //Status end

    //Process
    public static Process getProcess ()
    {
        return new Process(getUser(), getTask(), parse("2022-03-15 00:05:21"),
                parse("2022-05-12 12:08:23"), parse("2022-08-15 00:05:21"), getStatus());
    }

    public static Process getProcess (String year, int id)
    {
        Process process = new Process(getUser(), getTask(), parse("202" + year + "-03-15 00:05:21"),
                parse("2022-05-12 12:08:23"), parse("202" + year + "-08-15 00:05:21"), getStatus());
        process.setId(id);
        return process;
    }

    public static List<Process> getProcessList ()
    {
        return Arrays.asList(getProcess(), getProcess("1", 2));
    }
    //Process end

    //Conversation
    public static Conversation getConversation ()
    {
        return new Conversation(getUser("Dunay", true, false, 1),
                getProcess("2", 1), "Hello",
                parse("2022-05-13 22:37:05"));
    }

    public static Conversation getConversation (String message, int id)
    {
        Conversation conversation = new Conversation(getUser("Dunay", true, false, 1),
                getProcess("2", 1), message,
                parse("2022-05-13 22:37:05"));
        conversation.setId(id);
        return conversation;
    }

    public static List<Conversation> getConversationList ()
    {
        return Arrays.asList(getConversation("Hi", 1), getConversation("How", 2));
    }
    //Conversation end

    //EmailToken
    public static EmailToken getEmailToken ()
    {
        String date = "2022-05-11 12:08:26";
        EmailToken emailToken = new EmailToken("dunay@gmail", "123", parse(date));
        emailToken.setId(2);
        emailToken.setStatus(true);
        return emailToken;
    }

    public static EmailToken getEmailToken (String email, String token, Date expireTime, int id)
    {
        EmailToken emailToken = new EmailToken(email, token, expireTime);
        emailToken.setId(id);
        emailToken.setStatus(true);
        return emailToken;
    }

    public static List<EmailToken> getEmailTokenList ()
    {
        return Arrays.asList(getEmailToken(), getEmailToken("turqay@gmail", "555",
                parse("2023-06-05 16:43:22"), 3));
    }
    //EmailToken end

    //User
    public static User getUser (String name, Boolean status, Boolean banned, int id)
    {
        User user = new User(name, "Gudratli", "0556105884", "dunay@gmail", "git",
                "masazir", "cv", "dunay", getFile(), getCountry(), getRole());
        user.setId(id);
        user.setStatus(status);
        user.setBanned(banned);
        return user;
    }

    public static User getUser ()
    {
        User user = new User("Turqay", "Gudratli", "0556105884", "dunay@gmail", "git",
                "masazir", "cv", "dunay", getFile(), getCountry(), getRole());
        user.setStatus(true);
        user.setBanned(true);
        return user;
    }

    public static User getActiveUser (int id)
    {
        return getUser("Dunay", true, false, id);
    }

    public static User getBannedUser ()
    {
        return getUser("Dunay2", false, true, 7);
    }

    public static List<User> getUserList ()
    {
        return Arrays.asList(getActiveUser(6), getBannedUser(), getActiveUser(8));
    }

    public static List<User> getActiveUserList ()
    {
        return Arrays.asList(getActiveUser(6), getActiveUser(8));
    }

    public static List<User> getBannedUserList ()
    {
        return Collections.singletonList(getBannedUser());
    }
    //User end

    //Role
    public static Role getRole ()
    {
        Role role = new Role("USER", "User");
        role.setId(1);
        return role;
    }

    public static Role getRole (String name, String description, int id)
    {
        Role role = new Role(name, description);
        role.setId(id);
        return role;
    }

    public static List<Role> getRoleList ()
    {
        return Arrays.asList(getRole(), getRole("ADMIN", "Admin", 2));
    }
    //Role end

    //Date util methods
    @SneakyThrows
    public static Date parse (String date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.parse(date);
    }
}
