package com.unitech.backend.account.service;

import com.unitech.backend.account.model.Account;
import com.unitech.backend.account.model.AccountMapper;
import com.unitech.backend.account.repository.AccountRepository;
import com.unitech.backend.apiResponse.ApiResponseDTO;
import com.unitech.backend.enums.ResponseEnum;
import com.unitech.backend.user.model.UserDTO;
import com.unitech.backend.user.model.UserMapper;
import com.unitech.backend.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    public ApiResponseDTO addAccount(UserDTO userDTO){

        Optional<Long> lastIdOptional = accountRepository.lastAccountId();

        long lastId = lastIdOptional.orElse(1L);
        lastId++;

        Account account = new Account();
        account.setAccountBalance(BigDecimal.ZERO);
        account.setEnabled(true);
        account.setCreatedAt(LocalDateTime.now());
        account.setUser(UserMapper.toEntity(userDTO));
        account.setIdentifier(userDTO.getUsername().toUpperCase() + lastId);

        accountRepository.save(account);
        return ApiResponseDTO.success(account);
    }

    public ApiResponseDTO getEnabledAccountsByUser(long userId){
        List<Account> accounts = accountRepository.findUserAccounts(userId);
        return ApiResponseDTO.success(accounts.stream().map(AccountMapper::toDTO).toList());
    }

    public ApiResponseDTO disableAccount(long userId, long id){
        Optional<Account> accountOptional = accountRepository.findAccountByIdAndUser(id, userId);
        if (!accountOptional.isPresent()){
            return new ApiResponseDTO(ResponseEnum.ACCOUNT_NOT_FOUND_ERROR_MESSAGE.getMessage(), "");
        }

        Account result = accountOptional.get();
        result.setEnabled(false);
        accountRepository.save(result);

        return ApiResponseDTO.success(AccountMapper.toDTO(result));
    }

    public ApiResponseDTO addAccountBalance(String identifier, BigDecimal amount){
        if (amount.compareTo(BigDecimal.valueOf(1))<0){
            return new ApiResponseDTO(ResponseEnum.SMALL_AMOUNT_ERROR.getMessage(), "");
        }

        Optional<Account> accountOptional = accountRepository.findByIdentifier(identifier);

        if (!accountOptional.isPresent()){
            return new ApiResponseDTO(ResponseEnum.ACCOUNT_NOT_FOUND_ERROR_MESSAGE.getMessage(), "");
        }

        Account result = accountOptional.get();
        result.setAccountBalance(result.getAccountBalance().add(amount));

        accountRepository.save(result);

        return ApiResponseDTO.success(AccountMapper.toDTO(result));
    }
}
