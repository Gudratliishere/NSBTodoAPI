package com.gudratli.nsbtodoapi.exception.duplicate;

public class DuplicateLanguageException extends DuplicateException
{
    private final static String defaultErrorMessage = "Language already exists with this name.";

    public DuplicateLanguageException ()
    {
        this(defaultErrorMessage);
    }

    public DuplicateLanguageException (String message)
    {
        super(message);
    }
}
