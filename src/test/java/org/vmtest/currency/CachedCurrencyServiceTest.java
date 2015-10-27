package org.vmtest.currency;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.util.ReflectionTestUtils;
import org.vmtest.TestConfiguration;
import org.vmtest.currency.model.CurrencyRates;
import org.vmtest.currency.service.CachedCurrencyService;
import org.vmtest.currency.service.CurrencyService;
import org.vmtest.persistence.entity.CurrencyHistory;
import org.vmtest.persistence.service.CurrencyHistoryService;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

/**
 * Created by victor on 27.10.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class CachedCurrencyServiceTest {

    private final String sourceCurrency = "USD";

    private final String[] destinationCurrencies = new String[]{"GBP", "EUR", "CHF"};

    private final LocalDate todayDate = LocalDate.of(2015, 10, 27);

    @Autowired
    private CachedCurrencyService cacheService;

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
    public void shouldReturnCachedResultWhenCalledSecondTime() {
        when(currencyService.getExchangeRates(sourceCurrency, destinationCurrencies)).thenReturn(expectedRates);
        when(historyService.findHistoryByDateAndCurrency(todayDate, sourceCurrency)).thenReturn(historyRecord);

        CurrencyRates result = cacheService.getExchangeRates(sourceCurrency, destinationCurrencies);

        verify(historyService, times(1)).findHistoryByDateAndCurrency(todayDate, sourceCurrency);
        verify(currencyService, times(0)).getExchangeRates(sourceCurrency, destinationCurrencies);
        verify(historyService, times(0)).addHistory(historyRecord);
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

}
