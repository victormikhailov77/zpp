package org.vmtest.currency;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

/**
 * Created by victor on 23.10.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class OpenExchangeRestClientTest {

        @Autowired
        @Qualifier("openExchangeRestClient")
        CurrencyService service;

        private final String source = "USD";
        private final String[] currencies = new String[] {"GBP","EUR","CHF","SEK","NOK","PLN","CZK","DKK"};

        @Test
        public void shouldReturnLiveCurrencyRates() {

                CurrencyRates result = service.getExchangeRates(source, currencies);
                assertEquals("USD", result.getSource());
                assertEquals(8, result.getRates().size());
        }

        @Test
        public void shouldReturnHistoricalCurrencyRates() {

                CurrencyRates result = service.getExchangeRates(source, currencies, LocalDate.of(2015, 10, 19));
                assertEquals("USD", result.getSource());
                assertEquals(8, result.getRates().size());
        }

}