package com.github.nightdeveloper.smartdashboard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SmartDashboardApplication extends SpringBootServletInitializer {

    private static final Logger logger = LogManager.getLogger(SmartDashboardApplication.class);

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        logger.info("Configuring cors");
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SmartDashboardApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SmartDashboardApplication.class, args);
    }
}
