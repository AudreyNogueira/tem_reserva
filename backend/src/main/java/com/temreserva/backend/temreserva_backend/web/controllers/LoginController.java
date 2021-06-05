package com.temreserva.backend.temreserva_backend.web.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.temreserva.backend.temreserva_backend.business.CredentialBusiness;
import com.temreserva.backend.temreserva_backend.business.ImageBusiness;
import com.temreserva.backend.temreserva_backend.business.LoginBusiness;
import com.temreserva.backend.temreserva_backend.business.RestaurantBusiness;
import com.temreserva.backend.temreserva_backend.business.UserBusiness;
import com.temreserva.backend.temreserva_backend.data.repository.AddressRepository;
import com.temreserva.backend.temreserva_backend.data.repository.CredentialRepository;
import com.temreserva.backend.temreserva_backend.data.repository.ImageRepository;
import com.temreserva.backend.temreserva_backend.data.repository.ReserveRepository;
import com.temreserva.backend.temreserva_backend.data.repository.RestaurantRepository;
import com.temreserva.backend.temreserva_backend.data.repository.UserRepository;
import com.temreserva.backend.temreserva_backend.data.repository.RestaurantDateTimeRepository;
import com.temreserva.backend.temreserva_backend.web.model.dto.LoginDTO;
import com.temreserva.backend.temreserva_backend.web.model.response.LoginModel;

import org.springframework.http.HttpStatus;

@RestController
public class LoginController {
    private final LoginBusiness business;

    public LoginController(CredentialRepository credentialRepository, RestaurantRepository restaurantRepository,
            ImageRepository imageRepository, AddressRepository addressRepository, UserRepository userRepository,
            ReserveRepository reserveRepository, RestaurantDateTimeRepository restaurantDateTime) {
        business = new LoginBusiness(new CredentialBusiness(credentialRepository),
                new RestaurantBusiness(restaurantRepository, new CredentialBusiness(credentialRepository),
                        new ImageBusiness(imageRepository), addressRepository, reserveRepository, restaurantDateTime),
                new UserBusiness(userRepository, new ImageBusiness(imageRepository), new CredentialBusiness(credentialRepository)));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public LoginModel login(@RequestHeader("Authorization") String authorization,
            @RequestHeader("Content-Type") String contentType, @RequestBody LoginDTO login) {
        return business.login(login, authorization, contentType);
    }
}
