package com.gudratli.nsbtodoapi.exception.duplicate;

public class DuplicateUserTechnologyException extends DuplicateException
{
    private final static String defaultErrorMessage = "UserTechnology already exists with this user and technology.";

    public DuplicateUserTechnologyException ()
    {
        this(defaultErrorMessage);
    }

    public DuplicateUserTechnologyException (String message)
    {
        super(message);
    }
}
