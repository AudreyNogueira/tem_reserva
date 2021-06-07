package com.temreserva.backend.temreserva_backend.web.controllers;

import java.io.IOException;

import javax.validation.Valid;

import com.temreserva.backend.temreserva_backend.business.CredentialBusiness;
import com.temreserva.backend.temreserva_backend.business.ImageBusiness;
import com.temreserva.backend.temreserva_backend.business.UserBusiness;
import com.temreserva.backend.temreserva_backend.data.repository.CredentialRepository;
import com.temreserva.backend.temreserva_backend.data.repository.ImageRepository;
import com.temreserva.backend.temreserva_backend.data.repository.ReserveRepository;
import com.temreserva.backend.temreserva_backend.data.repository.UserRepository;
import com.temreserva.backend.temreserva_backend.web.model.dto.UserDTO;
import com.temreserva.backend.temreserva_backend.web.model.response.ImageModel;
import com.temreserva.backend.temreserva_backend.web.model.response.UserModel;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/user")
public class UserController {
    public final UserBusiness business;

    public UserController(UserRepository userRepository,CredentialRepository credentialRepository, ImageRepository imageRepository, ReserveRepository reserveRepository) {
        business = new UserBusiness(userRepository, new ImageBusiness(imageRepository), new CredentialBusiness(credentialRepository), reserveRepository);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public HttpStatus createNewUser(@RequestBody @Valid UserDTO dto) {
        return business.createNewUser(dto);
        
    }

    @PutMapping("/id={id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable Long id, @RequestBody UserDTO user) {
        business.updateUser(id, user);
    }

    @PostMapping("/upload")
    public ImageModel userImageUpload(@RequestParam("imageFile") MultipartFile file,
            @RequestParam("userId") Long id) throws IOException {
        return business.userImageUpload(file, id);
    }

    @GetMapping("/id={id}")
    @ResponseStatus(HttpStatus.OK)
    public UserModel getUserById(@PathVariable Long id) {
        return business.getUserById(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser( @PathVariable Long id ){
        business.deleteUser(id);
    }
}
