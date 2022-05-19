package com.gudratli.nsbtodoapi.exception.duplicate;

public class DuplicateProcessException extends DuplicateException
{
    private final static String defaultErrorMessage = "Process already exists with this user and task.";

    public DuplicateProcessException ()
    {
        this(defaultErrorMessage);
    }

    public DuplicateProcessException (String message)
    {
        super(message);
    }
}
