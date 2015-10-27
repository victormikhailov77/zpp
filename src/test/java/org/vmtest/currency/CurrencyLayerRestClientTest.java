package org.vmtest.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.vmtest.TestConfiguration;
import org.vmtest.currency.model.CurrencyRates;
import org.vmtest.currency.service.CurrencyService;
import org.vmtest.currency.service.currencylayer.CurrencyLayerRates;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by victor on 23.10.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class CurrencyLayerRestClientTest {


    @Autowired
    @Qualifier("currencyLayerRestClient")
    private CurrencyService service;

    private RestTemplate template = mock(RestTemplate.class);

    private static final String currencyLayerUrl = "http://apilayer.net/api/live?access_key=c57b2a6c21c9c041972558d6827c2458&source=USD&currencies=GBP,EUR,CHF,SEK,NOK,PLN,CZK,DKK";

    private static final String currencyLayerHistoricalUrl = "http://apilayer.net/api/historical?date=2015-10-19&access_key=c57b2a6c21c9c041972558d6827c2458&source=USD&currencies=GBP,EUR,CHF,SEK,NOK,PLN,CZK,DKK";

    private static final String currencyNotSupportedSourceUrl = "http://apilayer.net/api/live?access_key=c57b2a6c21c9c041972558d6827c2458&source=GBP&currencies=EUR,GBP,CHF,CZK,DKK,NOK,SEK,PLN";

    private static final String currencyNotSupportedDestinationUrl = "http://apilayer.net/api/live?access_key=c57b2a6c21c9c041972558d6827c2458&source=USD&currencies=DSA";

    private static final String currencyLayerExpectedJson = "{\"success\":true,\"terms\":\"https:\\/\\/currencylayer.com\\/terms\",\"privacy\":\"https:\\/\\/currencylayer.com\\/privacy\",\"timestamp\":1445566091,\"source\":\"USD\",\"quotes\":{\"USDGBP\":0.649098,\"USDEUR\":0.900657,\"USDCHF\":0.973302,\"USDSEK\":8.447798,\"USDNOK\":8.28665,\"USDPLN\":3.83175,\"USDCZK\":24.384501,\"USDDKK\":6.7184}}";

    private static final String notSupportedSourceCurrencyJson = "{\"success\":false,\"error\":{\"code\":105,\"info\":\"Access Restricted - Your current Subscription Plan does not support Source Currency Switching.\"}}";

    private static final String notSupportedDestinationCurrencyJson = "{\"success\":false,\"error\":{\"code\":202,\"info\":\"You have provided one or more invalid Currency Codes. [Required format: currencies=EUR,USD,GBP,...]\"}}";


    private final String source = "USD";
    private final String[] currencies = new String[] {"GBP","EUR","CHF","SEK","NOK","PLN","CZK","DKK"};
    private final String notSupportedSource = "GBP";
    private final String[] notSupportedCurrencies = new String[] {"DSA"};

    @Before
    public void initialize() throws IOException {
        ReflectionTestUtils.setField(service, "restTemplate", template);

        ObjectMapper mapper = new ObjectMapper();
        CurrencyLayerRates currencyLayerExpectedRates = mapper.readValue(currencyLayerExpectedJson, CurrencyLayerRates.class);
        CurrencyLayerRates notSupportedSource = mapper.readValue(notSupportedSourceCurrencyJson, CurrencyLayerRates.class);
        CurrencyLayerRates notSupportedDestination = mapper.readValue(notSupportedDestinationCurrencyJson, CurrencyLayerRates.class);

        when(template.getForObject(currencyLayerUrl, CurrencyLayerRates.class)).thenReturn(currencyLayerExpectedRates);
        when(template.getForObject(currencyLayerHistoricalUrl, CurrencyLayerRates.class)).thenReturn(currencyLayerExpectedRates);
        when(template.getForObject(currencyNotSupportedSourceUrl, CurrencyLayerRates.class)).thenReturn(notSupportedSource);
        when(template.getForObject(currencyNotSupportedDestinationUrl, CurrencyLayerRates.class)).thenReturn(notSupportedDestination);

    }

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

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionOnNonSupportedSourceCurrency() {
        CurrencyRates result = service.getExchangeRates(notSupportedSource, currencies);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionOnNonSupportedDestinationCurrency() {
        CurrencyRates result = service.getExchangeRates(source, notSupportedCurrencies);
    }
}
