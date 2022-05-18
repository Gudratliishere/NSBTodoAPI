package com.gudratli.nsbtodoapi.exception;

public class DuplicatePhoneException extends Exception
{
    private final static String defaultErrorMessage = "User already exists with this phone number.";

    public DuplicatePhoneException ()
    {
        this(defaultErrorMessage);
    }

    public DuplicatePhoneException (String message)
    {
        super(message);
    }
}
