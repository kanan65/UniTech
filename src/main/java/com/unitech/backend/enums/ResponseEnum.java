package com.unitech.backend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseEnum {
    ACCOUNT_NOT_FOUND_ERROR_MESSAGE("Account not found"),
    ACCOUNT_NOT_EXIST_ERROR_MESSAGE("You can't make transfer to non existing account."),
    AUTHENTICATION_FAILED_ERROR_MESSAGE("Authorization token is bad."),
    BAD_CREDENTIALS_ERROR_MESSAGE("Bad credentials."),
    CURRENCY_NOT_FOUND_ERROR("Currency not found. Use USD/AZN/RUB/EUR/TRY."),
    SUCCESS_MESSAGE("Success"),
    INSUFFICIENT_BALANCE_ERROR_MESSAGE("Not enough money on balance."),
    SAME_ACCOUNT_ERROR_MESSAGE("Accounts can't same."),
    DEACTIVATED_ACCOUNT_ERROR_MESSAGE("You can't make transfer to deactivated account."),
    WRONG_ACCOUNT_ERROR_MESSAGE("You can't transfer from foreign account."),
    SMALL_AMOUNT_ERROR("Transfer amount must be larger than 0."),
    SAME_PIN_ERROR_MESSAGE("There is a user with same pin."),
    USER_NOT_FOUND_ERROR_MESSAGE("This user not found."),
    UNKNOWN_ERROR("Unknown error.");

    private final String message;
}
