package com.unitech.backend.apiResponse;

import com.unitech.backend.enums.ResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class ApiResponseDTO implements Serializable {

    private String message;

    private Object data;

    public static ApiResponseDTO success() {
        return ApiResponseDTO.builder()
                .message(ResponseEnum.SUCCESS_MESSAGE.getMessage())
                .data(null)
                .build();
    }

    public static ApiResponseDTO success(Object data) {
        return ApiResponseDTO.builder()
                .message(ResponseEnum.SUCCESS_MESSAGE.getMessage())
                .data(data)
                .build();
    }
}
