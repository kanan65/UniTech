package com.unitech.backend.account.model;

import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public static AccountDTO toDTO(Account account) {
        if(account == null) return null;

        AccountDTO result = new AccountDTO();
        result.setId(account.getId());
        result.setEnabled(account.getEnabled());
        result.setIdentifier(account.getIdentifier());
        result.setAccountBalance(account.getAccountBalance());
        return result;
    }

    public static Account toEntity(AccountDTO accountDTO) {
        if(accountDTO == null) return null;

        Account result = new Account();
        result.setId(accountDTO.getId());
        result.setEnabled(accountDTO.getEnabled());
        result.setIdentifier(accountDTO.getIdentifier());
        result.setAccountBalance(accountDTO.getAccountBalance());
        return result;
    }
}
