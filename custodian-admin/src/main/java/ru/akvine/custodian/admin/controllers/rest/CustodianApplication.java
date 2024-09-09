package ru.akvine.custodian.admin.controllers.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "ru.akvine.custodian.core",
        "ru.akvine.custodian.admin",
        "ru.akvine.custodian.integration"})
@EntityScan(basePackages = "ru.akvine.custodian.core")
@EnableJpaRepositories(basePackages = "ru.akvine.custodian.core")
public class CustodianApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustodianApplication.class, args);
    }

}
