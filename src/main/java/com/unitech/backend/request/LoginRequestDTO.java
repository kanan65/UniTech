package com.unitech.backend.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class LoginRequestDTO implements Serializable {
    @NotNull(message = "Pin is required.")
    private String pin;

    @NotNull(message = "Password is required")
    private String password;
}
