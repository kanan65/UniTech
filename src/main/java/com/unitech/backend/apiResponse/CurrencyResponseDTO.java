package com.unitech.backend.apiResponse;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class CurrencyResponseDTO implements Serializable {

    private String currencies;

    private float rate;

    private String dateTime;

    public CurrencyResponseDTO(String currencies, float rate, String dateTime) {
        this.currencies = currencies;
        this.rate = rate;
        this.dateTime = dateTime;
    }
}
