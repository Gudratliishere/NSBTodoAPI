package com.gudratli.nsbtodoapi.exception.duplicate;

public class DuplicatePhoneException extends DuplicateException
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
