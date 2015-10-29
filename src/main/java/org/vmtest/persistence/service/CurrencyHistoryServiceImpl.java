package org.vmtest.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.vmtest.persistence.entity.CurrencyHistory;
import org.vmtest.persistence.repository.CurrencyHistoryRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by victor on 27.10.15.
 */
@Repository
public class CurrencyHistoryServiceImpl implements CurrencyHistoryService {

    @Autowired
    CurrencyHistoryRepository repository;

    @Override
    public void addHistory(CurrencyHistory newHistory) {
        repository.save(newHistory);
    }

    @Override
    public void updateHistory(CurrencyHistory newHistory) {
        repository.save(newHistory);
    }

    @Override
    public CurrencyHistory findHistoryByDateAndCurrency(LocalDate date, String currency) {
        List<CurrencyHistory> history = repository.findHistoryByDateAndCurrency(date, currency);

        return (history != null && !history.isEmpty()) ? history.get(0) : null;
    }
}
