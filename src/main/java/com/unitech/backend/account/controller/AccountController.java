package com.unitech.backend.account.controller;

import com.unitech.backend.account.model.AccountDTO;
import com.unitech.backend.account.service.AccountService;
import com.unitech.backend.apiResponse.ApiResponseDTO;
import com.unitech.backend.enums.ResponseEnum;
import com.unitech.backend.user.model.User;
import com.unitech.backend.user.model.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Locale;

@Controller
@AllArgsConstructor
@EnableScheduling
@RequestMapping(value = {"/account"})
public class AccountController {

    private final AccountService accountService;

    @GetMapping(value = "/getUserAccounts")
    public ApiResponseDTO getUserAccounts() {
        try {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return accountService.getEnabledAccountsByUser(currentUser.getId());
        } catch (Exception e) {
            return new ApiResponseDTO(ResponseEnum.AUTHENTICATION_FAILED_ERROR_MESSAGE.getMessage(), "");
        }
    }

    @PostMapping
    public ApiResponseDTO addAccount() {
        try {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (currentUser == null)
                return new ApiResponseDTO(ResponseEnum.AUTHENTICATION_FAILED_ERROR_MESSAGE.getMessage(), "");

            return accountService.addAccount(UserMapper.toDTO(currentUser));
        } catch (Exception e) {
            return new ApiResponseDTO(ResponseEnum.UNKNOWN_ERROR.getMessage(), "");
        }
    }

    @PostMapping(value = "/addBalance")
    public ApiResponseDTO addBalance(@RequestBody AccountDTO accountDTO) {
        try {
            return accountService.addAccountBalance(accountDTO.getIdentifier().toUpperCase(Locale.ROOT), accountDTO.getAmount());
        } catch (Exception e) {
            return new ApiResponseDTO(ResponseEnum.UNKNOWN_ERROR.getMessage(), "");
        }
    }

    @PostMapping(value = "/deactivate")
    public ApiResponseDTO disableAccount(@RequestParam(defaultValue = "0") Long accountId) {
        try {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (currentUser == null)
                return new ApiResponseDTO(ResponseEnum.AUTHENTICATION_FAILED_ERROR_MESSAGE.getMessage(), "");

            return accountService.disableAccount(currentUser.getId(), accountId);
        } catch (Exception e) {
            return new ApiResponseDTO(ResponseEnum.UNKNOWN_ERROR.getMessage(), "");
        }
    }
}