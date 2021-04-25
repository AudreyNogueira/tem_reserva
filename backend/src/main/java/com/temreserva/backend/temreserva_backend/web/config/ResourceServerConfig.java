package com.temreserva.backend.temreserva_backend.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        .antMatchers("/user/*").permitAll()        
        .antMatchers("/restaurant/create").permitAll()
        .antMatchers("/restaurant/id={id}").permitAll()
        .antMatchers("/restaurant/zone={zone}").permitAll()
        .antMatchers("/restaurant/name={name}").permitAll()
        .antMatchers("/restaurant/size={size}").permitAll()
        .antMatchers("/restaurant/login").permitAll()
        .antMatchers("/restaurant/home").permitAll()
        .antMatchers("/restaurant/upload").permitAll()
        .antMatchers("/restaurant/id={id}&idCredential={idCredential}").authenticated()
        // .antMatchers("/restaurant/upload").authenticated()
        .antMatchers("/reserve/*").authenticated()
        .antMatchers("/h2-console/**").permitAll()
        .anyRequest().denyAll()
        .and().headers().frameOptions().sameOrigin();
    }
}
