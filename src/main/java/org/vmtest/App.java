package org.vmtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class App {

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(App.class, args);
    }

}

