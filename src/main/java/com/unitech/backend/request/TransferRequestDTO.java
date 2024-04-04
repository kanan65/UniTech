package com.unitech.backend.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TransferRequestDTO implements Serializable {

    @NotNull
    @Size(min = 7)
    private String fromIdentifier;

    @NotNull
    @Size(min = 7)
    private String toIdentifier;

    @NotNull
    private BigDecimal amount;
}
