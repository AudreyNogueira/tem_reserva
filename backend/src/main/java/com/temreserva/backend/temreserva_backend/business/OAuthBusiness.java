package com.temreserva.backend.temreserva_backend.business;

import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class OAuthBusiness {
    public OAuthBusiness() {
    }

    // USED ONLY ON CREATE
    public String getAcessToken(String username, String password) {
        String credentials = "tem-reserva-backend:6740e91e-269c-4028-9f15-a10283d262a2";
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.ALL));
        headers.add("Authorization", "Basic " + encodedCredentials);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<String> request = new HttpEntity<String>(headers);
        String parameters = "?username=" + username + "&password=" + password + "&grant_type=password";
        return getToken(parameters, request);
    }

    // USED ONLY ON LOGIN
    public String getAcessToken(String username, String password, String authorization, String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.ALL));
        headers.add("Authorization", authorization);
        headers.add("Content-Type", contentType);

        HttpEntity<String> request = new HttpEntity<String>(headers);
        String parameters = "?username=" + username + "&password=" + password + "&grant_type=password";
        return getToken(parameters, request);
    }

    private String getToken(String parameters, HttpEntity<String> request) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String access_token_url = "http://localhost:8080/oauth/token";
            access_token_url += parameters;
            ResponseEntity<String> response = restTemplate.exchange(access_token_url, HttpMethod.POST, request,
                    String.class);
            return response.getBody().substring(response.getBody().indexOf(":") + 2,
                    response.getBody().indexOf(":") + 38);
        } catch (Exception ex) {
            return null;
        }
    }
}
