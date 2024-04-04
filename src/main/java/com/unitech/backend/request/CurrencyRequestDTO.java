package com.unitech.backend.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class CurrencyRequestDTO implements Serializable {
    
    @NotEmpty
    @NotNull
    private String from;

    @NotEmpty
    @NotNull
    private String to;

    public CurrencyRequestDTO(String string, String s, float rate) {
        this.from = from;
        this.to = to;
    }
}
