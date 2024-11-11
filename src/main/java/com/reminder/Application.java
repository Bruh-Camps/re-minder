package com.reminder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        // Inicializa o contexto do Spring e inicia o servidor embutido
        SpringApplication.run(Application.class, args);
    }
}
