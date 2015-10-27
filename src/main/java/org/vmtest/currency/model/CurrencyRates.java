package org.vmtest.currency.model;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrencyRates)) return false;

        CurrencyRates that = (CurrencyRates) o;

        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = timestamp != null ? timestamp.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        return result;
    }
}
