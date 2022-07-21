package com.gudratli.nsbtodoapi.dto;

import lombok.*;

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
}
