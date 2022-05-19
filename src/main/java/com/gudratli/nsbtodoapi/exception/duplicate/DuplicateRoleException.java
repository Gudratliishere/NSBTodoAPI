package com.gudratli.nsbtodoapi.exception.duplicate;

public class DuplicateRoleException extends DuplicateException
{
    private final static String defaultErrorMessage = "Role already exists with this name.";

    public DuplicateRoleException ()
    {
        this(defaultErrorMessage);
    }

    public DuplicateRoleException (String message)
    {
        super(message);
    }
}
