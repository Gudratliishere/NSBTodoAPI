package com.gudratli.nsbtodoapi.exception.duplicate;

public class DuplicateUserLanguageException extends DuplicateException
{
    private final static String defaultErrorMessage = "UserLanguage already exists with this user and language.";

    public DuplicateUserLanguageException ()
    {
        this(defaultErrorMessage);
    }

    public DuplicateUserLanguageException (String message)
    {
        super(message);
    }
}
