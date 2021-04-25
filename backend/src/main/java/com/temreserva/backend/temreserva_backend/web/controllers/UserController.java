package com.temreserva.backend.temreserva_backend.web.controllers;

import javax.validation.Valid;

import com.temreserva.backend.temreserva_backend.business.CredentialBusiness;
import com.temreserva.backend.temreserva_backend.business.UserBusiness;
import com.temreserva.backend.temreserva_backend.data.entity.User;
import com.temreserva.backend.temreserva_backend.data.repository.CredentialRepository;
import com.temreserva.backend.temreserva_backend.data.repository.UserRepository;
import com.temreserva.backend.temreserva_backend.web.model.DTOs.UserDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/user")
public class UserController {
    public final UserBusiness business;

    public UserController(UserRepository userRepository,CredentialRepository credentialRepository) {
        business = new UserBusiness(userRepository, new CredentialBusiness(credentialRepository));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public HttpStatus createNewUser(@RequestBody @Valid UserDTO dto) {
        return business.createNewUser(dto);
        
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public User userLogin(@RequestHeader("Authorization") String authorization, @RequestHeader("Content-Type") String contentType, @RequestBody String parameters) {
        return business.userLogin(parameters, authorization, contentType);
    }
}
