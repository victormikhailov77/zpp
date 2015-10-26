package org.vmtest.currency.currencylayer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.vmtest.currency.CurrencyRates;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/*
CurrencyLayer REST API client JSON response example

{
  "success":true,
  "terms":"https:\/\/currencylayer.com\/terms",
  "privacy":"https:\/\/currencylayer.com\/privacy",
  "timestamp":1445539628,
  "source":"USD",
  "quotes":
    {
       "USDGBP":0.649583,
       "USDEUR":0.899402,
       "USDCHF":0.97353,
       "USDSEK":8.45315,
       "USDNOK":8.28995,
       "USDPLN":3.83225,
       "USDCZK":24.346001,
       "USDDKK":6.709201
    }
 }

*/

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyLayerRates implements Serializable {
    private boolean success;

    private CurrencyLayerError error;

    private String terms;

    private String privacy;

    private Long timestamp;

    private String source;

    private Map<String, BigDecimal> quotes;

    public CurrencyLayerRates() {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public CurrencyLayerError getError() {
        return error;
    }

    public void setError(CurrencyLayerError error) {
        this.error = error;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

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

    public Map<String, BigDecimal> getQuotes() {
        return quotes;
    }

    public void setQuotes(Map<String, BigDecimal> quotes) {
        this.quotes = quotes;
    }

}
