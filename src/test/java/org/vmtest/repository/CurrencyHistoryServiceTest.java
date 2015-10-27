package org.vmtest.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.util.ReflectionTestUtils;
import org.vmtest.TestConfiguration;
import org.vmtest.currency.model.CurrencyRates;
import org.vmtest.persistence.entity.CurrencyHistory;
import org.vmtest.persistence.repository.CurrencyHistoryRepository;
import org.vmtest.persistence.service.CurrencyHistoryService;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

/**
 * Created by victor on 27.10.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class CurrencyHistoryServiceTest {

    private final String sourceCurrency = "USD";
    private final LocalDate todayDate = LocalDate.of(2015, 10, 27);

    private CurrencyHistory history = new CurrencyHistory(todayDate, sourceCurrency, mock(CurrencyRates.class));

    @Autowired
    @Qualifier("currencyHistoryServiceImpl")
    private CurrencyHistoryService service;

    private CurrencyHistoryRepository repository = mock(CurrencyHistoryRepository.class);

    @Before
    public void initialize() {
        ReflectionTestUtils.setField(service, "repository", repository);
    }

    @Test
    public void shouldAddNewHistory() {
        service.addHistory(history);
        verify(repository, times(1)).save(history);
    }

    @Test
    public void shouldReturnHistoryForGivenDateAndCurrency() {
        CurrencyHistory returnedHistory = service.findHistoryByDateAndCurrency(todayDate, sourceCurrency);
        verify(repository, times(1)).findHistoryByDateAndCurrency(todayDate, sourceCurrency);
    }
}
