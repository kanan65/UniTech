package com.unitech.backend.transfer.model;

import com.unitech.backend.account.model.AccountMapper;
import org.springframework.stereotype.Component;

@Component
public class TransferMapper {

    public static TransferDTO toDTO(Transfer transfer) {
        if(transfer == null) return null;

        TransferDTO result = new TransferDTO();
        result.setId(transfer.getId());
        result.setAmount(transfer.getAmount());
        result.setTo(AccountMapper.toDTO(transfer.getTo()));
        result.setFrom(AccountMapper.toDTO(transfer.getFrom()));
        result.setCreatedAt(transfer.getCreatedAt());

        return result;
    }

    public static Transfer toEntity(TransferDTO transferDTO) {
        if(transferDTO == null) return null;

        Transfer result = new Transfer();
        result.setId(transferDTO.getId());
        result.setAmount(transferDTO.getAmount());
        result.setCreatedAt(transferDTO.getCreatedAt());
        result.setFrom(AccountMapper.toEntity(transferDTO.getFrom()));
        result.setTo(AccountMapper.toEntity(transferDTO.getTo()));

        return result;
    }
}
