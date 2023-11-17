package com.acc.jobradar.config;

import jakarta.annotation.PostConstruct;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class SeleniumConfig {

    @PostConstruct
    void setDriver(){
        System.setProperty("webdriver.chrome.driver","./chromedriver");
    }

    @Bean
    public ChromeDriver chromeDriver(){
        return new ChromeDriver();
    }

    @Bean
    public WebDriverWait webDriverWait(){
        return new WebDriverWait(chromeDriver(), Duration.ofSeconds(20));
    }

}
