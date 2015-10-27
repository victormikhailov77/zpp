package org.vmtest;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.client.RestTemplate;
import org.vmtest.persistence.repository.CurrencyHistoryRepository;
import org.vmtest.persistence.repository.UserRepository;

import java.io.IOException;

import static org.mockito.Mockito.mock;

/**
 * Created by victor on 22.10.15.
 */
@Configuration
@ComponentScan(basePackages = {"org.vmtest.*"})
@PropertySources({
        @PropertySource("classpath:test.currencylayerapi.properties"),
        @PropertySource("classpath:test.openexchangeratesapi.properties")
})
public class TestConfiguration {

    @Bean
    static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    RestTemplate restTemplate() throws IOException {
        return mock(RestTemplate.class);
    }

    @Bean
    UserRepository getRepository() {
        return mock(UserRepository.class);
    }

    @Bean
    CurrencyHistoryRepository getCurrencyHistoryRepository() {
        return mock(CurrencyHistoryRepository.class);
    }

//    @Bean
//    CurrencyHistoryService getCurrencyHistoryService() {
//        return mock(CurrencyHistoryService.class);
//    }


}
