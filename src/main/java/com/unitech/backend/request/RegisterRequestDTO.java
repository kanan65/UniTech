package com.unitech.backend.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class RegisterRequestDTO {

    @NotNull
    @Size(min = 3, message = "Name must have minimum 3 characters.")
    private String name;

    @NotNull(message = "Pin required")
    @Size(min = 7, max = 7, message = "Pin must have 7 characters")
    private String pin;

    @NotNull
    @Size(min = 6, message = "Password must have minimum 6 characters")
    private String password;
}
