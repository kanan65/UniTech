package com.unitech.backend.currency.service;

import com.unitech.backend.apiResponse.ApiResponseDTO;
import com.unitech.backend.apiResponse.CurrencyResponseDTO;
import com.unitech.backend.currency.model.CurrencyDTO;
import com.unitech.backend.enums.ResponseEnum;
import com.unitech.backend.request.CurrencyRequestDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyService {

    public static CurrencyDTO currencyRates = new CurrencyDTO();

    public ApiResponseDTO getRate(CurrencyRequestDTO requestDTO){

        CurrencyDTO currencyRates = CurrencyService.currencyRates;

        if (currencyRates.getRates().get(requestDTO.getFrom().toLowerCase()) == null ||
            currencyRates.getRates().get(requestDTO.getTo().toLowerCase()) == null) {
            return new ApiResponseDTO(ResponseEnum.CURRENCY_NOT_FOUND_ERROR.getMessage(), "");
        }

        float rate = currencyRates.getRates().get(requestDTO.getTo().toLowerCase()) /
                     currencyRates.getRates().get(requestDTO.getFrom().toLowerCase());

        CurrencyResponseDTO currencyRatesResponse = new CurrencyResponseDTO(
                requestDTO.getFrom().toUpperCase() + "/" + requestDTO.getTo().toUpperCase(),
                rate,
                currencyRates.getDateTime().toString()

        );
        return ApiResponseDTO.success(currencyRatesResponse);
    }

    @Scheduled(fixedRate = 60*1000)
    public static void setRates(){

        /* Mocking Service */
        Map<String, Float> currencies = new HashMap<>();
        currencies.put("azn", 1.7F);
        currencies.put("usd", 1F);
        currencies.put("eur", 2.0F);
        currencies.put("try", 20.77F);

        currencyRates.setRates(currencies);
        currencyRates.setDateTime(LocalDateTime.now());
    }
}
