package org.vmtest.currency;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by victor on 23.10.15.
 */
public class CurrencyRates {
    private Long timestamp;

    private String source;

    private Map<String, BigDecimal> rates;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}
