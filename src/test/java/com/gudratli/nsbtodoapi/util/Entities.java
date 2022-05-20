package com.gudratli.nsbtodoapi.util;

import com.gudratli.nsbtodoapi.entity.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Entities
{
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

    public static List<UserLanguage> getUserLanguageList ()
    {
        return Arrays.asList(getUserLanguage(), getUserLanguage(10, 2));
    }

    public static User getActiveUser (int id)
    {
        return getUser("Dunay", true, false, id);
    }

    public static User getBannedUser ()
    {
        return getUser("Dunay2", false, true, 7);
    }

    public static User getUser (String name, Boolean status, Boolean banned, int id)
    {
        User user = new User(name, "Gudratli", "0556105884", "dunay@gmail", "git",
                "masazir", "cv", "dunay", "123", getCountry(), getRole());
        user.setId(id);
        user.setStatus(status);
        user.setBanned(banned);
        return user;
    }

    public static User getUser ()
    {
        User user = new User("Turqay", "Gudratli", "0556105884", "dunay@gmail", "git",
                "masazir", "cv", "dunay", "123", getCountry(), getRole());
        user.setStatus(true);
        user.setBanned(true);
        return user;
    }

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

    public static List<Technology> getTechnologyList ()
    {
        return Arrays.asList(getTechnology(), getTechnology("CSS", 2));
    }

    public static List<Language> getLanguageList ()
    {
        return Arrays.asList(getLanguage("Turkish", 1), getLanguage("English", 2));
    }

    public static List<Country> getCountryList ()
    {
        return Arrays.asList(getCountry("UK", 1), getCountry("Russia", 2));
    }

    public static List<Region> getRegionList ()
    {
        return Arrays.asList(getRegion("Asia", 1), getRegion("Europa", 2));
    }
}
