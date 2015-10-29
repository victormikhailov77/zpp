package org.vmtest.persistence.service;

import org.springframework.stereotype.Repository;
import org.vmtest.persistence.entity.CurrencyHistory;

import java.time.LocalDate;

/**
 * Created by victor on 27.10.15.
 */
@Repository
public interface CurrencyHistoryService {
    public void addHistory(CurrencyHistory newHistory);

    public void updateHistory(CurrencyHistory newHistory);

    public CurrencyHistory findHistoryByDateAndCurrency(LocalDate date, String currency);
}
