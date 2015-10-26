package org.vmtest.currency.currencylayer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by victor on 24.10.15.
 */

/*
Error structure JSON example
  {
    "code":202,
    "info":"You have provided one or more invalid Currency Codes. [Required format: currencies=EUR,USD,GBP,...]"
  }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyLayerError implements Serializable {

    private String code;

    private String info;

    public CurrencyLayerError() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
