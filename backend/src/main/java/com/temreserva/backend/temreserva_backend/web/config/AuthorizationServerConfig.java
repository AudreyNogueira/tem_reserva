package com.temreserva.backend.temreserva_backend.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Bean
    TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("tem-reserva-frontend").secret("b4fa16a4-dc5c-4d45-95ea-0cc29bb2def3")
                .scopes("read", "write").authorizedGrantTypes("password").accessTokenValiditySeconds(72000).and()
                .withClient("tem-reserva-backend").secret("6740e91e-269c-4028-9f15-a10283d262a2")
                .scopes("read", "write").authorizedGrantTypes("password").accessTokenValiditySeconds(72000);
    }
}
