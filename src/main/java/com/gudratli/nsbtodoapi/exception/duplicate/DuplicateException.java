package com.gudratli.nsbtodoapi.exception.duplicate;

public class DuplicateException extends Exception
{
    private final static String defaultErrorMessage = "Object already exists with this parameters.";

    public DuplicateException ()
    {
        this(defaultErrorMessage);
    }

    public DuplicateException (String message)
    {
        super(message);
    }
}
