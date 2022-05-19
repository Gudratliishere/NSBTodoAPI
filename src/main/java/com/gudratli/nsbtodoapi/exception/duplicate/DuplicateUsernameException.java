package com.gudratli.nsbtodoapi.exception.duplicate;

public class DuplicateUsernameException extends DuplicateException
{
    private final static String defaultErrorMessage = "User already exists with this username.";

    public DuplicateUsernameException ()
    {
        this(defaultErrorMessage);
    }

    public DuplicateUsernameException (String message)
    {
        super(message);
    }
}
