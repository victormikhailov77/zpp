package org.vmtest.currency;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.vmtest.currency.model.CurrencyRates;
import org.vmtest.currency.service.CachedCurrencyService;
import org.vmtest.currency.service.CurrencyService;
import org.vmtest.persistence.entity.CurrencyHistory;
import org.vmtest.persistence.service.CurrencyHistoryService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by victor on 27.10.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class CachedCurrencyServiceTest {

    private final String sourceCurrency = "USD";

    private final String[] destinationCurrencies = new String[]{"GBP", "EUR", "CHF"};

    private final LocalDate todayDate = LocalDate.now();

    private CachedCurrencyService cacheService = new CachedCurrencyService();

    private CurrencyHistoryService historyService = mock(CurrencyHistoryService.class);

    private CurrencyService currencyService = mock(CurrencyService.class);

    private CurrencyRates expectedRates = new CurrencyRates();

    private final CurrencyHistory historyRecord = new CurrencyHistory(todayDate, sourceCurrency, expectedRates);

    @Before
    public void initialize() {
        ReflectionTestUtils.setField(cacheService, "historyService", historyService);
        ReflectionTestUtils.setField(cacheService, "currencyService", currencyService);
        expectedRates.setSource(sourceCurrency);
        expectedRates.setTimestamp((long) todayDate.atStartOfDay().getSecond());
    }

    @Test
    public void shouldReachCurrencyServiceWhenCalledFirstTime() {
        when(currencyService.getExchangeRates(sourceCurrency, destinationCurrencies)).thenReturn(expectedRates);

        CurrencyRates result = cacheService.getExchangeRates(sourceCurrency, destinationCurrencies);

        verify(historyService, times(1)).findHistoryByDateAndCurrency(todayDate, sourceCurrency);
        verify(currencyService, times(1)).getExchangeRates(sourceCurrency, destinationCurrencies);
        verify(historyService, times(1)).addHistory(historyRecord);
    }

    @Test
    public void shouldReachCurrencyServiceAndUpdateCacheWhenCalledSecondTime() {
        when(currencyService.getExchangeRates(sourceCurrency, destinationCurrencies)).thenReturn(expectedRates);
        when(historyService.findHistoryByDateAndCurrency(todayDate, sourceCurrency)).thenReturn(historyRecord);

        CurrencyRates result = cacheService.getExchangeRates(sourceCurrency, destinationCurrencies);

        verify(historyService, times(1)).findHistoryByDateAndCurrency(todayDate, sourceCurrency);
        verify(currencyService, times(1)).getExchangeRates(sourceCurrency, destinationCurrencies);
        verify(historyService, times(1)).updateHistory(historyRecord);
    }

    @Test
    public void shouldReturnCachedResultWhenCalledSecondTimeAndFreshCachedDataAvailable() {
        LocalDateTime todayTime = LocalDateTime.now();
        LocalDateTime lastCachedTime = todayTime.minusMinutes(5);
        CurrencyRates cachedRates = new CurrencyRates();
        cachedRates.setSource(sourceCurrency);
        cachedRates.setTimestamp(lastCachedTime.toEpochSecond(ZoneOffset.UTC));
        CurrencyHistory cachedHistoryRecord = new CurrencyHistory(lastCachedTime.toLocalDate(), sourceCurrency, cachedRates);
        when(currencyService.getExchangeRates(sourceCurrency, destinationCurrencies)).thenReturn(cachedRates);
        when(historyService.findHistoryByDateAndCurrency(todayDate, sourceCurrency)).thenReturn(cachedHistoryRecord);

        CurrencyRates result = cacheService.getExchangeRates(sourceCurrency, destinationCurrencies);

        verify(historyService, times(1)).findHistoryByDateAndCurrency(todayDate, sourceCurrency);
        verify(currencyService, times(0)).getExchangeRates(sourceCurrency, destinationCurrencies);
        verify(historyService, times(0)).updateHistory(historyRecord);
    }

    @Test
    public void shouldReachCurrencyServiceForDateWhenCalledFirstTime() {
        when(currencyService.getExchangeRates(sourceCurrency, destinationCurrencies, todayDate)).thenReturn(expectedRates);

        CurrencyRates result = cacheService.getExchangeRates(sourceCurrency, destinationCurrencies, todayDate);

        verify(historyService, times(1)).findHistoryByDateAndCurrency(todayDate, sourceCurrency);
        verify(currencyService, times(1)).getExchangeRates(sourceCurrency, destinationCurrencies, todayDate);
        verify(historyService, times(1)).addHistory(historyRecord);
    }

    @Test
    public void shouldReturnCachedResultForDateWhenCalledSecondTime() {
        when(currencyService.getExchangeRates(sourceCurrency, destinationCurrencies, todayDate)).thenReturn(expectedRates);
        when(historyService.findHistoryByDateAndCurrency(todayDate, sourceCurrency)).thenReturn(historyRecord);

        CurrencyRates result = cacheService.getExchangeRates(sourceCurrency, destinationCurrencies, todayDate);

        verify(historyService, times(1)).findHistoryByDateAndCurrency(todayDate, sourceCurrency);
        verify(currencyService, times(0)).getExchangeRates(sourceCurrency, destinationCurrencies, todayDate);
        verify(historyService, times(0)).addHistory(historyRecord);
    }

    @Test
    public void shouldExecuteCurrencyService10Times() {
        Map<LocalDate, CurrencyRates> result = cacheService.getExchangeRatesHistoryBehindDate(sourceCurrency, destinationCurrencies, todayDate, 10);
        assertEquals(10, result.size());
    }

}
