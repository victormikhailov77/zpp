package org.vmtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.vmtest.persistence.repository.UserRepository;

@SpringBootApplication
@EnableWebMvc
@EnableMongoRepositories
public class App {

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(App.class, args);
    }

}

