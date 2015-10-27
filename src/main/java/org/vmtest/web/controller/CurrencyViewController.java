package org.vmtest.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.vmtest.currency.CurrencyRates;
import org.vmtest.currency.CurrencyService;
import org.vmtest.persistence.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Created by victor on 26.10.15.
 */
@Controller
@RequestMapping("/currencyview")
public class CurrencyViewController {

    private final String source = "USD";
    private final String[] currencies = new String[] {"GBP","EUR","CHF","SEK","NOK","PLN","CZK","DKK","HUF","RON"};

    @Autowired
    @Qualifier("currencyLayerRestClient")
    CurrencyService service;

    @RequestMapping(method = RequestMethod.GET)
    public String viewTodayCurrencyRates(ModelMap model) {
        CurrencyRates rates = service.getExchangeRates(source, currencies);
        model.addAttribute("currency", rates);
        model.addAttribute("today", LocalDateTime.now());
        model.addAttribute("quotetime", LocalDateTime.ofEpochSecond(rates.getTimestamp(), 0, ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        model.addAttribute("source", rates.getSource());
        model.addAttribute("currencies", currencies);

        Map<LocalDate, CurrencyRates> history = service.getExchangeRatesHistoryBehindDate(source, currencies, LocalDate.now(), 10);
        model.addAttribute("history", history);

        return "currencyview";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String userLogout(User user, Model model) {
        return "";
    }

}
