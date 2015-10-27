package org.vmtest.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.vmtest.persistence.entity.CurrencyHistory;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by victor on 27.10.15.
 */
@Repository
public interface CurrencyHistoryRepository extends MongoRepository<CurrencyHistory, LocalDate> {
    public List<CurrencyHistory> findHistoryByDate(LocalDate date);

    public List<CurrencyHistory> findHistoryByDateAndCurrency(LocalDate date, String currency);
}