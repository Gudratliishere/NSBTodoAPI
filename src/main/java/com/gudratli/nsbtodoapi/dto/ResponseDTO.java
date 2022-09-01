package com.gudratli.nsbtodoapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "Response", description = "Response DTO which used when data retrieve from api.")
public class ResponseDTO<T>
{
    @ApiModelProperty("Code of error if there is any error when returning data in API.")
    private Integer errorCode;

    @ApiModelProperty("Message about error if there is any error when returning data in API.")
    private String errorMessage;

    @ApiModelProperty("Message about success if data returned successfully.")
    private String successMessage;

    @NonNull
    @ApiModelProperty("Object that returned from API.")
    private T object;

    /**
     * Model is the name of entity
     */
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
