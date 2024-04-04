package com.unitech.backend.transfer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unitech.backend.account.model.Account;
import com.unitech.backend.account.model.AccountDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransferDTO {

    private Long id;
    private AccountDTO from;
    private AccountDTO to;
    private BigDecimal amount;
    private LocalDateTime createdAt = LocalDateTime.now();
}
