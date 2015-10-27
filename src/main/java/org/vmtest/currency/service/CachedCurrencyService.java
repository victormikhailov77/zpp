package org.vmtest.currency.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.vmtest.currency.model.CurrencyRates;
import org.vmtest.persistence.entity.CurrencyHistory;
import org.vmtest.persistence.service.CurrencyHistoryService;

import java.time.LocalDate;

/**
 * Created by victor on 27.10.15.
 * <p/>
 * Caching proxy around Currency Web Service
 */
@Service
public class CachedCurrencyService implements CurrencyService {

    @Autowired
    private CurrencyHistoryService historyService;

    @Autowired
    @Qualifier("currencyLayerRestClient")
    private CurrencyService currencyService;

    @Override
    public CurrencyRates getExchangeRates(String fromCurrency, String[] toCurrency) {

        LocalDate todayDate = LocalDate.now();
        CurrencyHistory history = historyService.findHistoryByDateAndCurrency(todayDate, fromCurrency);
        if (history == null) {
            CurrencyRates todayRate = currencyService.getExchangeRates(fromCurrency, toCurrency);
            history = new CurrencyHistory(todayDate, fromCurrency, todayRate);
            historyService.addHistory(history);
            return todayRate;
        }

        return history.getRates();
    }

    @Override
    public CurrencyRates getExchangeRates(String fromCurrency, String[] toCurrency, LocalDate date) {

        CurrencyHistory history = historyService.findHistoryByDateAndCurrency(date, fromCurrency);
        if (history == null) {
            CurrencyRates historyEntry = currencyService.getExchangeRates(fromCurrency, toCurrency, date);
            history = new CurrencyHistory(date, fromCurrency, historyEntry);
            historyService.addHistory(history);
            return historyEntry;
        }

        return history.getRates();
    }
}
