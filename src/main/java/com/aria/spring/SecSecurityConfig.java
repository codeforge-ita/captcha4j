package com.aria.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@ComponentScan(basePackages = { "com.aria.security" })
// @ImportResource({ "classpath:webSecurityConfig.xml" })
@EnableWebSecurity
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {

    public SecSecurityConfig() {
        super();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
    	http.csrf().disable();
    }

    // beans

}
