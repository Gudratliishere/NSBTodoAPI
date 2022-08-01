package com.gudratli.nsbtodoapi.dto;

import lombok.*;

import java.lang.reflect.ParameterizedType;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDTO<T>
{
    private Integer errorCode;
    private String errorMessage;
    private String successMessage;
    @NonNull
    private T object;

    /** Model is the name of entity */
    public ResponseDTO<T> notFound (String model, String parameter)
    {
        errorCode = 404;
        errorMessage = "There is not " + model + " with this " + parameter;

        return this;
    }

    public ResponseDTO<T> notFound (String message)
    {
        errorCode = 404;
        errorMessage = message;

        return this;
    }

    public ResponseDTO<T> duplicateException (String message)
    {
        errorCode = 304;
        errorMessage = message;

        return this;
    }

    public ResponseDTO<T> successfullyFetched (T object)
    {
        return saveParams(object, "Successfully fetched.");
    }

    public ResponseDTO<T> successfullyInserted (T object)
    {
        return saveParams(object, "Successfully inserted.");
    }

    public ResponseDTO<T> successfullyDeleted (T object)
    {
        return saveParams(object, "Successfully deleted.");
    }

    public ResponseDTO<T> successfullyUpdated (T object)
    {
        return saveParams(object, "Successfully updated.");
    }

    private ResponseDTO<T> saveParams (T object, String message)
    {
        this.object = object;
        successMessage = message;

        return this;
    }
}
