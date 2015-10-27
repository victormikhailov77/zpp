package org.vmtest.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.vmtest.currency.model.CurrencyRates;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;

/**
 * Created by victor on 27.10.15.
 */
public class CurrencyHistory implements Serializable {

    @Id
    private BigInteger id;

    @Indexed(unique = true)
    private LocalDate date;

    private String currency;

    private CurrencyRates rates;

    public CurrencyHistory() {
    }

    public CurrencyHistory(LocalDate date, String currency, CurrencyRates rates) {
        this.date = date;
        this.currency = currency;
        this.rates = rates;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public CurrencyRates getRates() {
        return rates;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setRates(CurrencyRates rates) {
        this.rates = rates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrencyHistory)) return false;

        CurrencyHistory that = (CurrencyHistory) o;

        if (currency != null ? !currency.equals(that.currency) : that.currency != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }
}
