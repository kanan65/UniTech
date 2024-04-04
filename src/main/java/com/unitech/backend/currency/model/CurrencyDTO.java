package com.unitech.backend.currency.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class CurrencyDTO implements Serializable {

    private Map<String, Float> rates;
    private LocalDateTime dateTime;

}
