package com.aria.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "com.aria.captcha.custom.service" })
public class ServiceConfig {
}
