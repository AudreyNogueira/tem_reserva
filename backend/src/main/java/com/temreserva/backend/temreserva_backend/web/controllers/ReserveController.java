package com.temreserva.backend.temreserva_backend.web.controllers;

import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import com.temreserva.backend.temreserva_backend.business.CredentialBusiness;
import com.temreserva.backend.temreserva_backend.business.ImageBusiness;
import com.temreserva.backend.temreserva_backend.business.ReserveBusiness;
import com.temreserva.backend.temreserva_backend.business.RestaurantBusiness;
import com.temreserva.backend.temreserva_backend.business.UserBusiness;
import com.temreserva.backend.temreserva_backend.data.repository.AddressRepository;
import com.temreserva.backend.temreserva_backend.data.repository.CredentialRepository;
import com.temreserva.backend.temreserva_backend.data.repository.ImageRepository;
import com.temreserva.backend.temreserva_backend.data.repository.MailTemplateRepository;
import com.temreserva.backend.temreserva_backend.data.repository.ReserveRepository;
import com.temreserva.backend.temreserva_backend.data.repository.RestaurantDateTimeRepository;
import com.temreserva.backend.temreserva_backend.data.repository.RestaurantRepository;
import com.temreserva.backend.temreserva_backend.data.repository.UserRepository;
import com.temreserva.backend.temreserva_backend.web.model.dto.ReserveDTO;
import com.temreserva.backend.temreserva_backend.web.model.response.ReserveModel;
import com.temreserva.backend.temreserva_backend.web.model.response.ReserveRestaurantModel;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/reserve")
public class ReserveController {
    public final ReserveBusiness business;

    public ReserveController(ReserveRepository reserveRepository, RestaurantRepository restaurantRepository,
            UserRepository userRepository, CredentialRepository credentialRepository, ImageRepository imageRepository,
            AddressRepository addressRepository, MailTemplateRepository mailTemplateRepository,
            RestaurantDateTimeRepository restaurantDateTime) {
        ImageBusiness imageBusiness = new ImageBusiness(imageRepository);
        CredentialBusiness credentialBusiness = new CredentialBusiness(credentialRepository);
        business = new ReserveBusiness(reserveRepository,
                new RestaurantBusiness(restaurantRepository, credentialBusiness, imageBusiness, addressRepository,
                        reserveRepository, restaurantDateTime),
                new UserBusiness(userRepository, imageBusiness, credentialBusiness, reserveRepository), mailTemplateRepository);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ReserveModel createNewReserve(@RequestBody @Valid ReserveDTO dto) throws MessagingException {
        return business.createNewReserve(dto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReserve(@PathVariable Long id) {
        business.deleteReserve(id);
    }

    @GetMapping(value = { "/restaurantId={restaurantId}" })
    @ResponseStatus(HttpStatus.OK)
    public List<ReserveRestaurantModel> getReservesByRestaurantId(@PathVariable Long restaurantId,
            @RequestBody(required = false) ReserveDTO reserve) {
        return business.getReservesByRestaurantId(restaurantId, reserve != null ? reserve.getReserveDate() : null);
    }

    @GetMapping("/userId={userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReserveModel> getReserveByUserId(@PathVariable Long userId,
            @RequestBody(required = false) ReserveDTO reserve) {
        return business.getReservesByUserId(userId, reserve != null ? reserve.getReserveDate() : null);
    }

    @GetMapping("/id={id}")
    @ResponseStatus(HttpStatus.OK)
    public ReserveModel getReserveById(@PathVariable Long id) {
        return business.getReserveById(id);
    }

    @PutMapping("/id={id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReserve(@PathVariable Long id, @RequestBody ReserveDTO reserve) {
        business.updateReserve(id, reserve);
    }
}
