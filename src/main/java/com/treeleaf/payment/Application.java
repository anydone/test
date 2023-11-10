package com.treeleaf.payment;

import com.oyster.service.Bootstrap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        JavaServiceMain javaServiceMain = context.getBean(JavaServiceMain.class);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.run(javaServiceMain);
    }

}
