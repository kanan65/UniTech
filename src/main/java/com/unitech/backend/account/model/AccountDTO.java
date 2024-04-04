package com.unitech.backend.account.model;

import com.unitech.backend.user.model.User;
import lombok.Data;
import java.math.BigDecimal;


@Data
public class AccountDTO {

    private Long id;
    private User user;
    private BigDecimal accountBalance;
    private String identifier;
    private BigDecimal amount;
    private Boolean enabled = true;

}
