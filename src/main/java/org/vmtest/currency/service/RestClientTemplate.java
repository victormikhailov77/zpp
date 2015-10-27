package org.vmtest.currency.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

/**
 * Created by victor on 22.10.15.
 */
public abstract class RestClientTemplate {

    @Autowired
    protected RestTemplate restTemplate;

    protected final String serviceUri;

    protected final String liveResource;

    protected final String historicalResource;

    protected final String apiKey;

    public RestClientTemplate(String serviceUri, String liveResource, String historicalResource, String apiKey) {
        this.serviceUri = serviceUri;
        this.liveResource = liveResource;
        this.historicalResource = historicalResource;
        this.apiKey = apiKey;
    }

    protected abstract String getApiKeyParameterName();

    protected abstract String getSourceCurrencyParameterName();

    protected abstract String getConvertedCurrenciesParameterName();

    protected String buildCurrencyList(String[] currencyList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < currencyList.length - 1; i++) {
            sb.append(currencyList[i]);
            sb.append(",");
        }
        sb.append(currencyList[currencyList.length - 1]);
        return sb.toString();
    }


    protected String buildLiveServiceUri(String source, String currencies) {
        StringBuilder sb = new StringBuilder();
        sb.append(serviceUri);
        sb.append(liveResource);
        sb.append("?");
        sb.append(getApiKeyParameterName());
        sb.append("=");
        sb.append(apiKey);
        buildParameter(sb, getSourceCurrencyParameterName(), source);
        buildCurrenciesParameter(currencies, sb);
        return sb.toString();
    }

    protected abstract String buildHistoricalServiceUri(String source, String currencies, LocalDate date);

    protected void buildCurrenciesParameter(String currencies, StringBuilder sb) {
        buildParameter(sb, getConvertedCurrenciesParameterName(), currencies);
    }

    protected void buildFirstParameter(StringBuilder sb, String parameterName, String parameterValue) {
        sb.append("?");
        sb.append(parameterName);
        sb.append("=");
        sb.append(parameterValue);
    }

    protected void buildParameter(StringBuilder sb, String parameterName, String parameterValue) {
        sb.append("&");
        sb.append(parameterName);
        sb.append("=");
        sb.append(parameterValue);
    }
}
