package com.temreserva.backend.temreserva_backend.business;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.temreserva.backend.temreserva_backend.data.entity.*;
import com.temreserva.backend.temreserva_backend.data.repository.AddressRepository;
import com.temreserva.backend.temreserva_backend.data.repository.RestaurantRepository;
import com.temreserva.backend.temreserva_backend.web.model.DTOs.RestaurantDTO;
import com.temreserva.backend.temreserva_backend.web.model.Responses.AddressModel;
import com.temreserva.backend.temreserva_backend.web.model.Responses.HomeRestaurantsModel;
import com.temreserva.backend.temreserva_backend.web.model.Responses.RestaurantModel;
import com.temreserva.backend.temreserva_backend.web.model.Responses.ZoneRestaurantModel;
import com.temreserva.backend.temreserva_backend.web.model.Responses.ZoneRestaurantsViewModel;
import com.temreserva.backend.temreserva_backend.web.utils.Enumerators;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Service
@Transactional
public class RestaurantBusiness {
    private final RestaurantRepository restaurantRepository;
    private final CredentialBusiness credentialBusiness;
    private final OAuthBusiness oauthBusiness;
    private final AddressRepository addressRepository;
    private final ImageBusiness imageBusiness;

    @Autowired
    public RestaurantBusiness(RestaurantRepository restaurantRepository, CredentialBusiness credentialBusiness,
            ImageBusiness imageBusiness, AddressRepository addressRepository) {
        this.restaurantRepository = restaurantRepository;
        this.credentialBusiness = credentialBusiness;
        this.imageBusiness = imageBusiness;
        this.oauthBusiness = new OAuthBusiness();
        this.addressRepository = addressRepository;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // BUSINESS
    // ------------------------------------------------------------------------------------------------------------------------------------------

    public Restaurant restaurantLogin(String parameters, String authorization, String contentType) {
        String username = parameters.substring(parameters.indexOf("=") + 1, parameters.indexOf("&")).replace("%40",
                "@");
        String password = parameters.substring(parameters.indexOf("&") + 10, parameters.indexOf("&grant_type"));
        String accessToken = oauthBusiness.getAcessToken(username, password, authorization, contentType);

        if (accessToken != null) {
            Credential restaurantCredentials = credentialBusiness.getCredentialByEmail(username);
            Restaurant restaurant = restaurantRepository.findByCredential(restaurantCredentials);
            // restaurant.setAccessToken(accessToken);
            restaurant.getCredential().setPassword(null);
            return restaurant;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.USERNAME_OR_PASSWORD_INVALID.getEnumValue());
    }

    private Restaurant validateNewRestaurantDto(RestaurantDTO dto) {
        if (credentialBusiness.validateNewCredential(dto.getEmail(), dto.getPassword()))
            return new Restaurant(dto);
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    Enumerators.apiExceptionCodeEnum.CREATE_EXISTING_USER.getEnumValue());
    }

    public HttpStatus restaurantImageUpload(MultipartFile file, Long id, Boolean isProfilePic) throws IOException {
        if (restaurantRepository.findById(id).orElse(null) != null)
            return imageBusiness.imageUpload(file, id, isProfilePic, true);

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.RESTAURANT_NOT_FOUND.getEnumValue());
    }

    public HomeRestaurantsModel getRestaurantForHome() {
        try {
            List<ZoneRestaurantModel> zoneRestaurants = getZoneRestaurantListForHome(
                    addressRepository.findByDistinctZoneNames());
            List<RestaurantModel> topRatedRestaurantsModel = getListOfRestaurant(
                    restaurantRepository.findByAverageStars(10));

            return HomeRestaurantsModel.builder().zone(zoneRestaurants).topRated(topRatedRestaurantsModel).build();
        } catch (Exception ex) {
            return HomeRestaurantsModel.builder().zone(new ArrayList<ZoneRestaurantModel>())
                    .topRated(new ArrayList<RestaurantModel>()).build();
        }
    }

    public ZoneRestaurantsViewModel getRestaurantByZone(String zone) {
        try {
            List<RestaurantModel> allRestaurants = getRestaurantModelListByZone(zone, false);
            List<RestaurantModel> topRestaurants = allRestaurants.stream()
                    .sorted(Comparator.comparing(RestaurantModel::getAverageStars).reversed())
                    .collect(Collectors.toList());

            topRestaurants = topRestaurants.size() < 10 ? topRestaurants.subList(0, topRestaurants.size())
                    : topRestaurants.subList(0, 10);

            return ZoneRestaurantsViewModel.builder().zoneName(zone).restaurantList(allRestaurants)
                    .topRated(topRestaurants).build();
        } catch (Exception ex) {
            return ZoneRestaurantsViewModel.builder().zoneName(zone).restaurantList(new ArrayList<RestaurantModel>())
                    .topRated(new ArrayList<RestaurantModel>()).build();
        }
    }

    public HttpStatus createNewRestaurant(RestaurantDTO dto) {
        Restaurant restaurant = validateNewRestaurantDto(dto);
        Address address = getAddressByDto(dto);
        if (address != null || restaurant != null)
            restaurant.setCredential(credentialBusiness.createNewCredential(dto.getEmail(), dto.getPassword()));

        if (restaurant.getCredential() != null)
            return createRestaurant(restaurant, address);

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.CREATE_CREDENTIAL_ERROR.getEnumValue());
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // CREATE
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private HttpStatus createRestaurant(Restaurant restaurant, Address address) {
        try {
            restaurantRepository.save(restaurant);
            address.setRestaurant(restaurant);
            addressRepository.save(address);
            return HttpStatus.CREATED;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    Enumerators.apiExceptionCodeEnum.DEFAULT_ERROR.getEnumValue());
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // UPDATE
    // ------------------------------------------------------------------------------------------------------------------------------------------
    @Transactional(rollbackOn = ResponseStatusException.class)
    public void updateRestaurant(Long id, RestaurantDTO restaurant) {
        Restaurant restaurantResponse = restaurantRepository.findById(id).map(r -> {
            r.setCnpj(restaurant.getCnpj() == null ? r.getCnpj() : restaurant.getCnpj());
            r.setRestaurantName(restaurant.getRestaurantName() == null ? r.getRestaurantName() : restaurant.getRestaurantName());
            r.setOpenDaysOfWeek(
                    restaurant.getOpenDaysOfWeek() == null ? r.getOpenDaysOfWeek() : restaurant.getOpenDaysOfWeek());
            r.setOpeningTime(restaurant.getOpeningTime() == null ? r.getOpeningTime() : restaurant.getOpeningTime());
            r.setClosingTime(restaurant.getClosingTime() == null ? r.getClosingTime() : restaurant.getClosingTime());
            r.setSpacingOfTables(
                    restaurant.getSpacingOfTables() == null ? r.getSpacingOfTables() : restaurant.getSpacingOfTables());
            r.setHandicappedAdapted(restaurant.getHandicappedAdapted() == null ? r.getHandicappedAdapted()
                    : restaurant.getHandicappedAdapted());
            r.setCleaningPeriodicity(restaurant.getCleaningPeriodicity() == null ? r.getCleaningPeriodicity()
                    : restaurant.getCleaningPeriodicity());
            r.setMaxNumberOfPeople(restaurant.getMaxNumberOfPeople() == null ? r.getMaxNumberOfPeople()
                    : restaurant.getMaxNumberOfPeople());
            r.setActualNumberOfPeople(restaurant.getActualNumberOfPeople() == null ? r.getActualNumberOfPeople()
                    : restaurant.getActualNumberOfPeople());
            r.setAverageStars(
                    restaurant.getAverageStars() == null ? r.getAverageStars() : restaurant.getAverageStars());
            r.setUpdateDate(LocalDateTime.now());
            r.setPhoneNumber(restaurant.getPhoneNumber() == null ? r.getPhoneNumber() : restaurant.getPhoneNumber());
            credentialBusiness.updateEmailByID(r.getCredential().getId(), restaurant.getEmail());            
            return restaurantRepository.save(r);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                Enumerators.apiExceptionCodeEnum.RESTAURANT_NOT_FOUND.getEnumValue()));

        if(restaurant.getAddress() != null){
            Address currentAddress = addressRepository.findByRestaurant(restaurantResponse);
            Address address = getAddressByDto(restaurant);
            address.setRestaurant(currentAddress.getRestaurant());
            address.setId(currentAddress.getId());
            addressRepository.save(address);
        }

        if (restaurant.getActualPassword() != null && restaurant.getPassword() != null) {
            if (restaurantResponse.getCredential().getPassword().equals(restaurant.getActualPassword()))
                credentialBusiness.updatePasswordById(restaurantResponse.getCredential().getId(),
                        restaurant.getPassword());
            else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        Enumerators.apiExceptionCodeEnum.WRONG_PASSWORD.getEnumValue());
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // GET
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public RestaurantModel getRestaurantById(long id) {
        return restaurantRepository.findById(id).map(r -> {
            return RestaurantModel.builder().id(id).email(r.getCredential().getEmail())
                    .averageStars(r.getAverageStars())
                    .address(getAddressModelFromAddress(addressRepository.findByRestaurant(r)))
                    .actualNumberOfPeople(r.getActualNumberOfPeople()).maxNumberOfPeople(r.getMaxNumberOfPeople())
                    .restaurantName(r.getRestaurantName())
                    .restaurantImages(imageBusiness.getRestaurantImagesByOwner(id))
                    .profileImage(imageBusiness.getProfileImageByOwnerId(id, true)).phoneNumber(r.getPhoneNumber())
                    .build();
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                Enumerators.apiExceptionCodeEnum.RESTAURANT_NOT_FOUND.getEnumValue()));
    }

    public List<Restaurant> getRestaurantByName(String name) {
        return restaurantRepository.findByName(name);
    }

    private AddressModel getAddressModelFromAddress(Address address) {
        AddressModel response = AddressModel.builder().locality(address.getLocality())
                .restaurantNumber(address.getRestaurantNumber()).uf(address.getUf()).cep(address.getCep())
                .address(address.getAddress()).complement(address.getComplement()).district(address.getDistrict())
                .zone(address.getZone()).build();

        return response;
    }

    private Address getAddressByDto(RestaurantDTO dto) {
        return Address.builder().district(dto.getAddress().getDistrict()).address(dto.getAddress().getAddress())
                .locality(dto.getAddress().getLocality()).uf(dto.getAddress().getUf())
                .restaurantNumber(dto.getAddress().getRestaurantNumber()).zone(dto.getAddress().getZone())
                .cep(dto.getAddress().getCep()).complement(dto.getAddress().getComplement()).build();
    }

    private List<RestaurantModel> getListOfRestaurant(List<Restaurant> restaurants) {
        List<RestaurantModel> response = new ArrayList<RestaurantModel>();

        for (Restaurant restaurant : restaurants) {
            try {
                Address address = addressRepository.findByRestaurant(restaurant);
                byte[] img = imageBusiness.getProfileImageByOwnerId(address.getRestaurant().getId(), true);
                RestaurantModel model = RestaurantModel.builder().id(restaurant.getId())
                        .email(restaurant.getCredential().getEmail()).profileImage(img)
                        .address(getAddressModelFromAddress(address)).restaurantName(restaurant.getRestaurantName())
                        .actualNumberOfPeople(restaurant.getActualNumberOfPeople())
                        .maxNumberOfPeople(restaurant.getMaxNumberOfPeople()).averageStars(restaurant.getAverageStars())
                        .phoneNumber(restaurant.getPhoneNumber()).build();

                response.add(model);
            } catch (Exception ex) {
                continue;
            }
        }

        return response;
    }

    private List<ZoneRestaurantModel> getZoneRestaurantListForHome(List<String> zoneNames) {
        List<ZoneRestaurantModel> response = new ArrayList<ZoneRestaurantModel>();

        for (String zoneName : zoneNames) {
            try {
                List<RestaurantModel> retaurantModels = getRestaurantModelListByZone(zoneName, true);
                ZoneRestaurantModel zoneRestaurantModel = ZoneRestaurantModel.builder().zoneName(zoneName)
                        .restaurantList(retaurantModels).build();

                response.add(zoneRestaurantModel);
            } catch (Exception e) {
                continue;
            }
        }

        return response;
    }

    private List<RestaurantModel> getRestaurantModelListByZone(String zoneName, Boolean isHome) {
        try {
            List<Address> restaurantsAddress = isHome ? addressRepository.findByZoneForHome(zoneName)
                    : addressRepository.findByZone(zoneName);
            List<Restaurant> restaurants = new ArrayList<Restaurant>();
            restaurantsAddress.forEach(x -> restaurants.add(x.getRestaurant()));
            List<RestaurantModel> retaurantModels = getListOfRestaurant(restaurants);
            return retaurantModels;
        } catch (Exception ex) {
            return new ArrayList<RestaurantModel>();
        }
    }

    public List<Restaurant> getRestaurantBySize(Integer size) {
        List<Restaurant> response = restaurantRepository.findBySize(size);

        if (response != null)
            return response;

        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                Enumerators.apiExceptionCodeEnum.RESTAURANT_NOT_FOUND.getEnumValue());
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // GET
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void deleteRestaurant(Long id) {
        restaurantRepository.findById(id).map(r -> {
            Long idCred = r.getCredential().getId();
            imageBusiness.deleteImageByOwnerId(id);
            addressRepository.delete(addressRepository.findByRestaurant(r));
            restaurantRepository.delete(r);
            credentialBusiness.deleteCredentialById(idCred);
            return Void.TYPE;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.RESTAURANT_NOT_FOUND.getEnumValue()));
    }
}
