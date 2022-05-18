package com.gudratli.nsbtodoapi.exception;

public class DuplicateEmailException extends Exception
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
