package org.vmtest.currency;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by victor on 23.10.15.
 */
@Service
public interface CurrencyService {
    public CurrencyRates getExchangeRates(String fromCurrency, String[] toCurrency);

    public CurrencyRates getExchangeRates(String fromCurrency, String[] toCurrency, LocalDate date);

    default public Map<LocalDate, CurrencyRates> getExchangeRatesHistoryBehindDate(String fromCurrency, String[] toCurrency, LocalDate date, int days) {
        Map<LocalDate, CurrencyRates> result = new LinkedHashMap<>();
        LocalDate lastDate = date;
        for (int day = 0; day < days; day++) {
            LocalDate pastDate = lastDate.minusDays(day);
            CurrencyRates rates = getExchangeRates(fromCurrency, toCurrency, pastDate);
            result.put(pastDate, rates);
        }
        return result;
    }

}
