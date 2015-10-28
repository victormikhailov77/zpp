package org.vmtest;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by victor on 22.10.15.
 */
@Configuration
@ComponentScan(basePackages = {"org.vmtest.*"})
@PropertySources({
        @PropertySource("classpath:currencylayerapi.properties"),
        @PropertySource("classpath:openexchangeratesapi.properties")
})
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    static RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("currencyview");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/currencyview").setViewName("currencyview");
    }

}
