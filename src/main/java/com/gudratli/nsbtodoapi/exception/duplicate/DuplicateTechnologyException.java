package com.gudratli.nsbtodoapi.exception.duplicate;

public class DuplicateTechnologyException extends DuplicateException
{
    private final static String defaultErrorMessage = "Object already exists with this parameters.";

    public DuplicateTechnologyException ()
    {
        this(defaultErrorMessage);
    }

    public DuplicateTechnologyException (String message)
    {
        super(message);
    }
}
