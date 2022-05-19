package com.gudratli.nsbtodoapi.exception.duplicate;

public class DuplicateRegionException extends  DuplicateException
{
    private final static String defaultErrorMessage = "Region already exists with this name.";

    public DuplicateRegionException ()
    {
        this(defaultErrorMessage);
    }

    public DuplicateRegionException (String message)
    {
        super(message);
    }
}
