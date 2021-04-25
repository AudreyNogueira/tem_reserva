package com.temreserva.backend.temreserva_backend.web.controllers;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import com.temreserva.backend.temreserva_backend.business.CredentialBusiness;
import com.temreserva.backend.temreserva_backend.business.ImageBusiness;
import com.temreserva.backend.temreserva_backend.business.RestaurantBusiness;
import com.temreserva.backend.temreserva_backend.data.entity.Restaurant;
import com.temreserva.backend.temreserva_backend.data.repository.AddressRepository;
import com.temreserva.backend.temreserva_backend.data.repository.CredentialRepository;
import com.temreserva.backend.temreserva_backend.data.repository.ImageRepository;
import com.temreserva.backend.temreserva_backend.data.repository.RestaurantRepository;
import com.temreserva.backend.temreserva_backend.web.model.DTOs.RestaurantDTO;
import com.temreserva.backend.temreserva_backend.web.model.Responses.HomeRestaurantsModel;
import com.temreserva.backend.temreserva_backend.web.model.Responses.ZoneRestaurantsViewModel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantBusiness business;

    public RestaurantController(RestaurantRepository restaurantRepository, CredentialRepository credentialRepository,
            ImageRepository imageRepository, AddressRepository adressRepository) {
        business = new RestaurantBusiness(restaurantRepository, new CredentialBusiness(credentialRepository),
                new ImageBusiness(imageRepository), adressRepository);
    }

    @PostMapping("/create")
    public HttpStatus createNewRestaurant(@RequestBody @Valid RestaurantDTO dto) {
        return business.createNewRestaurant(dto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Restaurant restaurantLogin(@RequestHeader("Authorization") String authorization,
            @RequestHeader("Content-Type") String contentType, @RequestBody String parameters) {
        return business.restaurantLogin(parameters, authorization, contentType);
    }

    @PostMapping("/upload")
    public HttpStatus restaurantImageUpload(@RequestParam("imageFile") MultipartFile file,
            @RequestParam("restaurantId") Long id) throws IOException {
        return business.restaurantImageUpload(file, id);
    }

    @PutMapping("/id={id}&idCredential={idCredential}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRestaurant(@PathVariable Long id, @PathVariable Long idUser, @RequestBody Restaurant restaurant) {
        business.updateRestaurant(id, idUser, restaurant);
    }

    @GetMapping("/name={name}")
    @ResponseStatus(HttpStatus.OK)
    public List<Restaurant> getRestaurantByName(@PathVariable String name) {
        return business.getRestaurantByName(name);
    }

    @GetMapping("/size={size}")
    @ResponseStatus(HttpStatus.OK)
    public List<Restaurant> getRestaurantBySize(@PathVariable Integer size) {
        return business.getRestaurantBySize(size);
    }

    @GetMapping("/home")
    @ResponseStatus(HttpStatus.OK)
    public HomeRestaurantsModel getRestaurantForHome() {
        return business.getRestaurantForHome();
    }

    @GetMapping("/zone={zone}")
    @ResponseStatus(HttpStatus.OK)
    public ZoneRestaurantsViewModel getRestaurantByZone(@PathVariable String zone) {
        return business.getRestaurantByZone(zone);
    }

    @GetMapping("/id={id}")
    @ResponseStatus(HttpStatus.OK)
    public Restaurant getRestaurantById(@PathVariable Long id) {
        return business.getRestaurantById(id);
    }
}