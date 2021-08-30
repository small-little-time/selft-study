package com.time.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class TimeSpringBootStarterDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeSpringBootStarterDemoApplication.class, args);
    }


}
