package com.gudratli.nsbtodoapi.exception.duplicate;

public class DuplicateEmailException extends DuplicateException
{
    private final static String defaultErrorMessage = "User already exists with this email.";

    public DuplicateEmailException ()
    {
        this(defaultErrorMessage);
    }

    public DuplicateEmailException (String message)
    {
        super(message);
    }
}
