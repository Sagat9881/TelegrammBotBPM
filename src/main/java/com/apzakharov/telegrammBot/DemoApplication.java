package com.apzakharov.telegrammBot;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableJpaRepositories
@EnableProcessApplication
//@EnableScheduling
public class DemoApplication {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(DemoApplication.class, args);
    }
}

//TODO: BackLog:
//  1) Форматировать все переменные. (Все с большой, либо все с маленькой)
//  2) Убрать возвращающиеся NULL из декораторов над методами репозиториев в сервисных классах
//

