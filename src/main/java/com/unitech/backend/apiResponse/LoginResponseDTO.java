package com.unitech.backend.apiResponse;

import lombok.Data;
import java.io.Serializable;

@Data
public class LoginResponseDTO implements Serializable {
    private String token;
}
