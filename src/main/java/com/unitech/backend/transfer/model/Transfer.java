package com.unitech.backend.transfer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unitech.backend.account.model.Account;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "transfer")
public class Transfer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "from_account", referencedColumnName = "id")
    private Account from;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "to_account", referencedColumnName = "id")
    private Account to;

    private BigDecimal amount;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

}
