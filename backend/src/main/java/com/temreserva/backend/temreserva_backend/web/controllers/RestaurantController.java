package com.temreserva.backend.temreserva_backend.web.controllers;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import com.temreserva.backend.temreserva_backend.business.CredentialBusiness;
import com.temreserva.backend.temreserva_backend.business.ImageBusiness;
import com.temreserva.backend.temreserva_backend.business.RestaurantBusiness;
import com.temreserva.backend.temreserva_backend.data.repository.AddressRepository;
import com.temreserva.backend.temreserva_backend.data.repository.CredentialRepository;
import com.temreserva.backend.temreserva_backend.data.repository.ImageRepository;
import com.temreserva.backend.temreserva_backend.data.repository.ReserveRepository;
import com.temreserva.backend.temreserva_backend.data.repository.RestaurantDateTimeRepository;
import com.temreserva.backend.temreserva_backend.data.repository.RestaurantRepository;
import com.temreserva.backend.temreserva_backend.web.model.dto.RestaurantDTO;
import com.temreserva.backend.temreserva_backend.web.model.response.HomeRestaurantsModel;
import com.temreserva.backend.temreserva_backend.web.model.response.ImageModel;
import com.temreserva.backend.temreserva_backend.web.model.response.RestaurantModel;
import com.temreserva.backend.temreserva_backend.web.model.response.ZoneRestaurantsViewModel;

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
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantBusiness business;

    public RestaurantController(RestaurantRepository restaurantRepository, CredentialRepository credentialRepository,
            ImageRepository imageRepository, AddressRepository adressRepository, ReserveRepository reserveRepository,
            RestaurantDateTimeRepository restaurantDateTime) {
        business = new RestaurantBusiness(restaurantRepository, new CredentialBusiness(credentialRepository),
                new ImageBusiness(imageRepository), adressRepository, reserveRepository, restaurantDateTime);
    }

    @PostMapping("/create")
    public HttpStatus createNewRestaurant(@RequestBody @Valid RestaurantDTO dto) {
        return business.createNewRestaurant(dto);
    }

    @PostMapping("/upload")
    public ImageModel restaurantImageUpload(@RequestParam("imageFile") MultipartFile file,
            @RequestParam("restaurantId") Long id, @RequestParam("isProfilePic") Boolean isProfilePic)
            throws IOException {
        return business.restaurantImageUpload(file, id, isProfilePic);
    }

    @PutMapping("/id={id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRestaurant(@PathVariable Long id, @RequestBody RestaurantDTO restaurant) {
        business.updateRestaurant(id, restaurant);
    }

    @GetMapping("/name={name}")
    @ResponseStatus(HttpStatus.OK)
    public List<RestaurantModel> getRestaurantByName(@PathVariable String name) {
        return business.getRestaurantByName(name);
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
    public RestaurantModel getRestaurantById(@PathVariable Long id) {
        return business.getRestaurantById(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRestaurant(@PathVariable Long id) {
        business.deleteRestaurant(id);
    }

    @DeleteMapping(value = "/image/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@PathVariable Long id) {
        business.deleteImage(id);
    }
}