package com.gudratli.nsbtodoapi.exception.duplicate;

public class DuplicateCountryException extends DuplicateException
{
    private final static String defaultErrorMessage = "Country already exists with this name and region.";

    public DuplicateCountryException ()
    {
        this(defaultErrorMessage);
    }

    public DuplicateCountryException (String message)
    {
        super(message);
    }
}
