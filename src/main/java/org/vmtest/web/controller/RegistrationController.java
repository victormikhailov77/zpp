package org.vmtest.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.vmtest.persistence.entity.User;
import org.vmtest.persistence.service.UserService;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by victor on 24.10.15.
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String newRegistration(ModelMap model) {
        User user = new User();
        model.put(BindingResult.MODEL_KEY_PREFIX + "user",
                model.get("errors"));
        model.addAttribute("user", user);
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveRegistration(@Valid User user, BindingResult result, ModelMap model,
                                   final RedirectAttributes redirectAttributes) {


        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", result);
            redirectAttributes.addFlashAttribute("user", user);
            return "register";
        }

        userService.addNewUser(user);
        model.addAttribute("success", "Dear " + user.getFirstName() + " , your Registration completed successfully");
        return "redirect:/login";
    }

    @ModelAttribute("countries")
    public Map<String, String> initializeCountries() {

        // Populate dropdown with list of countries, sorted by display name
        // NOTE: country names are locale-specific
        Map<String, String> countries = new TreeMap<>();
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            Locale obj = new Locale("EN-US", countryCode);
            countries.put(obj.getDisplayCountry(), obj.getCountry());
        }

        return countries;
    }

}
