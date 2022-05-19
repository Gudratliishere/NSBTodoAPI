package com.gudratli.nsbtodoapi.exception.duplicate;

public class DuplicateStatusException extends DuplicateException
{
    private final static String defaultErrorMessage = "Status already exists with this name.";

    public DuplicateStatusException ()
    {
        this(defaultErrorMessage);
    }

    public DuplicateStatusException (String message)
    {
        super(message);
    }
}
