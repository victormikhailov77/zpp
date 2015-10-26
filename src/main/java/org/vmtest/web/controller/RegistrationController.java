package org.vmtest.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.vmtest.web.model.User;

import javax.validation.Valid;
import java.util.*;

/**
 * Created by victor on 24.10.15.
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String newRegistration(ModelMap model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveRegistration(@Valid User user, Model model) {
            //(@Valid User user, BindingResult result, ModelMap model){

//        if(result.hasErrors()) {
//            return "register";
//        }


        ObjectMapper mapper = new ObjectMapper();
        String userJson = "";
        try {
            userJson = mapper.writeValueAsString(user);
        }
        catch(Exception e) {
            logger.error(e.toString());
        }
        logger.info("User registered :" + userJson);
        model.addAttribute("success", "Dear "+ user.getFirstName()+" , your Registration completed successfully");
        return "redirect:/login";
    }


    @ModelAttribute("countries")
    public Map<String, String> initializeCountries() {

        Map<String, String> countries = new TreeMap<>();

        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            countries.put(obj.getDisplayCountry(), obj.getCountry());
        }

        return countries;
    }

}
