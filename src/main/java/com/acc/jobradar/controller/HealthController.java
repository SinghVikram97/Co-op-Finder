package com.acc.jobradar.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/isActive")
    public String isActive(){
        return "Service is up and running";
    }
}
