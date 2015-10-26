package org.vmtest.currency.currencylayer;

import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.vmtest.currency.CurrencyRates;
import org.vmtest.currency.CurrencyService;
import org.vmtest.currency.RestClientTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * CurrencyLayer REST API client
 * see description at;
 * https://currencylayer.com/documentation
 *
 * Created by victor on 22.10.15.
 */
@Service
public class CurrencyLayerRestClient extends RestClientTemplate implements CurrencyService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyLayerRestClient.class);

    @Autowired
    public CurrencyLayerRestClient(@Value("${currencylayer.service.uri}") String serviceUri,
            @Value("${currencylayer.live.resource}") String liveResource,
            @Value("${currencylayer.historical.resource}") String historicalResource,
            @Value("${currencylayer.api.key}") String apiKey) {
        super(serviceUri, liveResource, historicalResource, apiKey);
    }

    @Override
    public CurrencyRates getExchangeRates(String fromCurrency, String[] toCurrency) {
        final String uri = buildLiveServiceUri(fromCurrency, buildCurrencyList(toCurrency));
        return getCurrencyRatesFromRestApi(uri);
    }

    @Override
    public CurrencyRates getExchangeRates(String fromCurrency, String[] toCurrency, LocalDate date) {
        final String uri = buildHistoricalServiceUri(fromCurrency, buildCurrencyList(toCurrency), date);
        return getCurrencyRatesFromRestApi(uri);
    }

    private CurrencyRates getCurrencyRatesFromRestApi(String uri) {
        CurrencyLayerRates rates = null;

        try {
            rates = restTemplate.getForObject(uri, CurrencyLayerRates.class);
            if (!rates.isSuccess()) {
                String formattedError = "CurrencyLayer API Error " + rates.getError().getCode() + " " + rates.getError().getInfo();
                logger.error(formattedError);
                throw new RuntimeException(formattedError);
            }
        }
        catch (RestClientException e) {
            logger.error(e.toString());
            throw e;
        }

        Map<String, BigDecimal> filteredResult = new LinkedHashMap();
        // transform result as "USDGBP":0.649583 -> "GBP":0.649583
        rates.getQuotes().forEach( (key, value) -> filteredResult.put(key.substring(3, 6), value));
        CurrencyRates result = new CurrencyRates();
        result.setTimestamp(rates.getTimestamp());
        result.setSource(rates.getSource());
        result.setRates(filteredResult);

        return result;
    }

    @Override
    protected String getApiKeyParameterName() {
        return "access_key";
    }

    @Override
    protected String getSourceCurrencyParameterName() {
        return "source";
    }

    @Override
    protected String getConvertedCurrenciesParameterName() {
        return "currencies";
    }

    @Override
    protected String buildHistoricalServiceUri(String source, String currencies, LocalDate date) {
        StringBuilder sb = new StringBuilder();
        sb.append(serviceUri);
        sb.append(historicalResource);
        buildFirstParameter(sb, "date", date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        buildParameter(sb, getApiKeyParameterName(), apiKey);
        buildParameter(sb, getSourceCurrencyParameterName(), source);
        buildCurrenciesParameter(currencies, sb);
        return sb.toString();
    }
}
