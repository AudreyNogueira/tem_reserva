package com.temreserva.backend.temreserva_backend.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    WebConfig webConfig;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // http.authorizeRequests()
        // .antMatchers("/user/create").permitAll()
        // .antMatchers("/user/*").authenticated()
        // .antMatchers("/restaurant/create").permitAll()
        // .antMatchers("/restaurant/*").authenticated()
        // .antMatchers("/restaurant/image/*").authenticated()
        // .antMatchers("/reserve/*").authenticated().antMatchers("/h2-console/**").permitAll()
        // .antMatchers("/login/**").permitAll().anyRequest().denyAll().and().headers().frameOptions()
        // .sameOrigin();

        http
        .csrf().disable()
        .cors().configurationSource(webConfig.corsConfigurationSource()).and()
        .authorizeRequests()
        .antMatchers("/user/create").permitAll()
        .antMatchers("/user/*").authenticated()
        .antMatchers("/restaurant/create").permitAll()
        .antMatchers("/restaurant/*").authenticated()
        .antMatchers("/restaurant/image/*").authenticated()
        .antMatchers("/reserve/*").authenticated().antMatchers("/h2-console/**").permitAll()
        .antMatchers("/login/**").permitAll();
    }
}
