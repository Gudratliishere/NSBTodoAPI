package com.gudratli.nsbtodoapi.exception;

public class DuplicateUsernameException extends Exception
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
