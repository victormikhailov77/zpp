package org.vmtest.currency.openexchangerates;

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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Open Exchange Rates REST API client
 * see description at:
 * https://openexchangerates.org/documentation
 *
 * Created by victor on 22.10.15.
 */
@Service
public class OpenExchangeRestClient extends RestClientTemplate implements CurrencyService {

    private static final Logger logger = LoggerFactory.getLogger(OpenExchangeRestClient.class);

    private final boolean freeApi;

    @Autowired
    public OpenExchangeRestClient(@Value("${openexchange.service.uri}") String serviceUri,
                                  @Value("${openexchange.live.resource}") String liveResource,
                                  @Value("${openexchange.historical.resource}") String historicalResource,
                                  @Value("${openexchange.api.key}") String apiKey,
                                  @Value("${openexchange.free.api}") String freeApi) {
        super(serviceUri, liveResource, historicalResource, apiKey);
        this.freeApi= freeApi != null && freeApi.equals("true") ? true : false;
    }

    @Override
    public CurrencyRates getExchangeRates(String fromCurrency, String[] toCurrency) {
        final String uri = buildLiveServiceUri(fromCurrency, buildCurrencyList(toCurrency));
        return getCurrencyRatesFromRestApi(uri, toCurrency);
    }

    @Override
    public CurrencyRates getExchangeRates(String fromCurrency, String[] toCurrency, LocalDate date) {
        final String uri = buildHistoricalServiceUri(fromCurrency, buildCurrencyList(toCurrency), date);
        return getCurrencyRatesFromRestApi(uri, toCurrency);
    }

    private CurrencyRates getCurrencyRatesFromRestApi(String uri, String[] toCurrency) {
        try {
            final OpenExchangeRates rates = restTemplate.getForObject(uri, OpenExchangeRates.class);
            if (rates.isError()) {
                String formattedError = "OpenExchange API Error: code=" + rates.getStatus() +
                    "; message=" + rates.getMessage() +
                    "; description=" + rates.getDescription();
                logger.error(formattedError);
                throw new RuntimeException(formattedError);
            }

            // in free API selected currency symbols not supported, all possible currencies returned
            // emulate selective query via output filtering
            if (freeApi) {
                Map<String, BigDecimal> filteredRates = new LinkedHashMap<>();
                Arrays.asList(toCurrency).forEach(s -> filteredRates.put(s, rates.getRates().get(s)));
                rates.setRates(filteredRates);
            }

            CurrencyRates result = new CurrencyRates();
            result.setTimestamp(rates.getTimestamp());
            result.setSource(rates.getBase());
            result.setRates(rates.getRates());

            return result;
        }
        catch (RestClientException e) {
            logger.error(e.toString());
            throw e;
        }
    }

    @Override
    protected String getApiKeyParameterName() {
        return "app_id";
    }

    @Override
    protected String getSourceCurrencyParameterName() {
        return "base";
    }

    @Override
    protected String getConvertedCurrenciesParameterName() {
        return "symbols";
    }

    // in free API this parameter causes request failure
    @Override
    protected void buildCurrenciesParameter(String currencies, StringBuilder sb) {
        if(!freeApi) {
            super.buildCurrenciesParameter(currencies, sb);
        }
    }

    @Override
    protected String buildHistoricalServiceUri(String source, String currencies, LocalDate date) {
        StringBuilder sb = new StringBuilder();
        sb.append(serviceUri);
        sb.append(replacePlaceholderWithDate(historicalResource, date));
        buildFirstParameter(sb, getApiKeyParameterName(), apiKey);
        buildParameter(sb, getSourceCurrencyParameterName(), source);
        buildCurrenciesParameter(currencies, sb);
        return sb.toString();
    }

    private String replacePlaceholderWithDate(String historicalResource, LocalDate date) {
        String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        return historicalResource.replaceAll("%date%", dateStr);
    }
}
