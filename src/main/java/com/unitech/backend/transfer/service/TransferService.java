package com.unitech.backend.transfer.service;

import com.unitech.backend.account.model.Account;
import com.unitech.backend.account.model.AccountMapper;
import com.unitech.backend.account.repository.AccountRepository;
import com.unitech.backend.apiResponse.ApiResponseDTO;
import com.unitech.backend.enums.ResponseEnum;
import com.unitech.backend.transfer.model.TransferDTO;
import com.unitech.backend.transfer.model.TransferMapper;
import com.unitech.backend.transfer.repository.TransferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TransferService {

    private final TransferRepository transferRepository;

    private final AccountRepository accountRepository;

    @Transactional
    public ApiResponseDTO transfer(long userId, String fromIdentifier, String toIdentifier, BigDecimal amount){
        if (amount.compareTo(BigDecimal.valueOf(1)) < 0){
            return new ApiResponseDTO(ResponseEnum.SMALL_AMOUNT_ERROR.getMessage(), "");
        }

        Optional<Account> fromUserAccount = accountRepository.findAccountByIdentifierAndUser(fromIdentifier.toUpperCase(), userId);

        if (!fromUserAccount.isPresent()){
            return new ApiResponseDTO(ResponseEnum.WRONG_ACCOUNT_ERROR_MESSAGE.getMessage(), "");
        }

        else if (fromUserAccount.get().getAccountBalance().compareTo(amount) < 0){
            return new ApiResponseDTO(ResponseEnum.INSUFFICIENT_BALANCE_ERROR_MESSAGE.getMessage(), "");
        }

        Optional<Account> toUserAccount = accountRepository.findByIdentifier(toIdentifier.toUpperCase());

        if (toUserAccount.get().getIdentifier().equals(fromUserAccount.get().getIdentifier())){
            return new ApiResponseDTO(ResponseEnum.SAME_ACCOUNT_ERROR_MESSAGE.getMessage(), "");
        }

        else if (!toUserAccount.isPresent()){
            return new ApiResponseDTO(ResponseEnum.ACCOUNT_NOT_EXIST_ERROR_MESSAGE.getMessage(), "");
        }
        else if (!toUserAccount.get().getEnabled()){
            return new ApiResponseDTO(ResponseEnum.DEACTIVATED_ACCOUNT_ERROR_MESSAGE.getMessage(), "");
        }

        Account fromAccount = fromUserAccount.get();
        Account toAccount = toUserAccount.get();

        fromAccount.setAccountBalance(fromAccount.getAccountBalance().subtract(amount));
        toAccount.setAccountBalance(toAccount.getAccountBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        TransferDTO result = new TransferDTO();
        result.setFrom(AccountMapper.toDTO(fromAccount));
        result.setTo(AccountMapper.toDTO(toAccount));
        result.setAmount(amount);
        result.setCreatedAt(LocalDateTime.now());

        transferRepository.save(TransferMapper.toEntity(result));

        return ApiResponseDTO.success(result);
    }
}
