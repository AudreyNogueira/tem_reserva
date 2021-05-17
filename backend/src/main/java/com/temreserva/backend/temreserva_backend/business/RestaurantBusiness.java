package com.temreserva.backend.temreserva_backend.business;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.temreserva.backend.temreserva_backend.data.entity.*;
import com.temreserva.backend.temreserva_backend.data.repository.AddressRepository;
import com.temreserva.backend.temreserva_backend.data.repository.ReserveRepository;
import com.temreserva.backend.temreserva_backend.data.repository.RestaurantDateTimeRepository;
import com.temreserva.backend.temreserva_backend.data.repository.RestaurantRepository;
import com.temreserva.backend.temreserva_backend.web.model.dto.RestaurantDTO;
import com.temreserva.backend.temreserva_backend.web.model.dto.RestaurantDateTimeDTO;
import com.temreserva.backend.temreserva_backend.web.model.interfaces.ICurrentPeopleModel;
import com.temreserva.backend.temreserva_backend.web.model.response.AddressModel;
import com.temreserva.backend.temreserva_backend.web.model.response.CurrentPeopleModel;
import com.temreserva.backend.temreserva_backend.web.model.response.HomeRestaurantsModel;
import com.temreserva.backend.temreserva_backend.web.model.response.ImageModel;
import com.temreserva.backend.temreserva_backend.web.model.response.RestaurantDateTimeModel;
import com.temreserva.backend.temreserva_backend.web.model.response.RestaurantModel;
import com.temreserva.backend.temreserva_backend.web.model.response.ZoneRestaurantModel;
import com.temreserva.backend.temreserva_backend.web.model.response.ZoneRestaurantsViewModel;
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
    private final RestaurantDateTimeRepository restaurantDateTimeRepository;
    private final CredentialBusiness credentialBusiness;
    private final AddressRepository addressRepository;
    private final ImageBusiness imageBusiness;
    private final ReserveRepository reserveRepository;

    @Autowired
    public RestaurantBusiness(RestaurantRepository restaurantRepository, CredentialBusiness credentialBusiness,
            ImageBusiness imageBusiness, AddressRepository addressRepository, ReserveRepository reserveRepository,
            RestaurantDateTimeRepository restaurantDateTimeRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantDateTimeRepository = restaurantDateTimeRepository;
        this.credentialBusiness = credentialBusiness;
        this.imageBusiness = imageBusiness;
        this.addressRepository = addressRepository;
        this.reserveRepository = reserveRepository;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // BUSINESS
    // ------------------------------------------------------------------------------------------------------------------------------------------

    private Restaurant validateNewRestaurantDto(RestaurantDTO dto) {
        if (credentialBusiness.validateNewCredential(dto.getEmail(), dto.getPassword()))
            return new Restaurant(dto);
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    Enumerators.apiExceptionCodeEnum.CREATE_EXISTING_USER.getEnumValue());
    }

    public ImageModel restaurantImageUpload(MultipartFile file, Long id, Boolean isProfilePic) throws IOException {
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
            return createRestaurant(restaurant, address, dto.getRestaurantDateTime());

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.CREATE_CREDENTIAL_ERROR.getEnumValue());
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // CREATE
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private HttpStatus createRestaurant(Restaurant restaurant, Address address,
            List<RestaurantDateTimeDTO> restaurantDateTime) {
        try {
            restaurantRepository.save(restaurant);
            address.setRestaurant(restaurant);
            addressRepository.save(address);
            if (restaurantDateTime != null)
                restaurantDateTime.forEach(r -> createRestaurantDateTime(restaurant, r));
            return HttpStatus.CREATED;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    Enumerators.apiExceptionCodeEnum.DEFAULT_ERROR.getEnumValue());
        }
    }

    private void createRestaurantDateTime(Restaurant restaurant, RestaurantDateTimeDTO dto) {
        try {
            restaurantDateTimeRepository.save(RestaurantDateTime.builder().restaurant(restaurant).day(dto.getDay())
                    .openingTime(dto.getOpeningTime()).closingTime(dto.getClosingTime()).build());
        } catch (Exception ex) {
            throw ex;
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // UPDATE
    // ------------------------------------------------------------------------------------------------------------------------------------------
    @Transactional(rollbackOn = Exception.class)
    public void updateRestaurant(Long id, RestaurantDTO restaurant) {
        Restaurant restaurantResponse = restaurantRepository.findById(id).map(r -> {
            r.setCnpj(restaurant.getCnpj() == null ? r.getCnpj() : restaurant.getCnpj());
            r.setDescription(restaurant.getDescription() == null ? r.getDescription() : restaurant.getDescription());
            r.setRestaurantName(
                    restaurant.getRestaurantName() == null ? r.getRestaurantName() : restaurant.getRestaurantName());
            r.setSpacingOfTables(
                    restaurant.getSpacingOfTables() == null ? r.getSpacingOfTables() : restaurant.getSpacingOfTables());
            r.setHandicappedAdapted(restaurant.getHandicappedAdapted() == null ? r.getHandicappedAdapted()
                    : restaurant.getHandicappedAdapted());
            r.setCleaning(restaurant.getCleaning() == null ? r.getCleaning() : restaurant.getCleaning());
            r.setMaxNumberOfPeople(restaurant.getMaxNumberOfPeople() == null ? r.getMaxNumberOfPeople()
                    : restaurant.getMaxNumberOfPeople());
            r.setActualNumberOfPeople(restaurant.getActualNumberOfPeople() == null ? r.getActualNumberOfPeople()
                    : restaurant.getActualNumberOfPeople());
            r.setAverageStars(
                    restaurant.getAverageStars() == null ? r.getAverageStars() : restaurant.getAverageStars());
            r.setUpdateDate(LocalDateTime.now());
            r.setPhoneNumber(restaurant.getPhoneNumber() == null ? r.getPhoneNumber() : restaurant.getPhoneNumber());
            r.setPayment(restaurant.getPayment() == null ? r.getPayment() : restaurant.getPayment());
            restaurantDateTimeRepository.findByRestaurant(r).forEach(a -> restaurantDateTimeRepository.delete(a));
            restaurant.getRestaurantDateTime().forEach(rest -> createRestaurantDateTime(r, rest));
            credentialBusiness.updateEmailByID(r.getCredential().getId(), restaurant.getEmail());
            return restaurantRepository.save(r);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                Enumerators.apiExceptionCodeEnum.RESTAURANT_NOT_FOUND.getEnumValue()));

        if (restaurant.getAddress() != null) {
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
        return restaurantRepository.findById(id).map(restaurant -> {
            Address address = addressRepository.findByRestaurant(restaurant);
            ImageModel img = imageBusiness.getProfileImageByOwnerId(address.getRestaurant().getId(), true);
            List<ImageModel> lstImages = imageBusiness.getRestaurantImagesByOwner(address.getRestaurant().getId());
            List<ICurrentPeopleModel> currentPeople = reserveRepository
                    .findCurrentPeopleModelByRestaurant(restaurant.getId(), LocalDate.now());
            List<RestaurantDateTimeModel> dateTime = getListRestaurantDateTimeModel(
                    restaurantDateTimeRepository.findByRestaurant(restaurant));
            return RestaurantModel.builder().id(restaurant.getId()).email(restaurant.getCredential().getEmail())
                    .profileImage(img).cnpj(restaurant.getCnpj()).cleaning(restaurant.getCleaning())
                    .address(getAddressModelFromAddress(address)).restaurantName(restaurant.getRestaurantName())
                    .currentPeople(getListCurrentPeopleModel(currentPeople)).restaurantImages(lstImages)
                    .maxNumberOfPeople(restaurant.getMaxNumberOfPeople()).averageStars(restaurant.getAverageStars())
                    .payment(restaurant.getPayment()).phoneNumber(restaurant.getPhoneNumber())
                    .description(restaurant.getDescription()).restaurantDateTime(dateTime).build();
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                Enumerators.apiExceptionCodeEnum.RESTAURANT_NOT_FOUND.getEnumValue()));
    }

    public Restaurant findByCredential(Credential credential) {
        return restaurantRepository.findByCredential(credential);
    }

    public List<RestaurantDateTimeModel> getListRestaurantDateTimeModel(List<RestaurantDateTime> list) {
        List<RestaurantDateTimeModel> response = new ArrayList<RestaurantDateTimeModel>();

        for (RestaurantDateTime rest : list) {
            RestaurantDateTimeModel model = RestaurantDateTimeModel.builder().day(rest.getDay())
                    .closingTime(rest.getClosingTime()).openingTime(rest.getOpeningTime()).build();
            response.add(model);
        }

        return response;
    }

    public List<CurrentPeopleModel> getListCurrentPeopleModel(List<ICurrentPeopleModel> iCurrentPeopleModels) {
        List<CurrentPeopleModel> response = new ArrayList<CurrentPeopleModel>();
        for (ICurrentPeopleModel current : iCurrentPeopleModels) {
            try {
                response.add(CurrentPeopleModel.builder().currentPeople(current.getCurrentPeople())
                        .period(current.getPeriod()).build());
            } catch (Exception e) {
                continue;
            }
        }

        return response;
    }

    public List<RestaurantModel> getRestaurantByName(String name) {
        return getListOfRestaurant(restaurantRepository.findByName(name));
    }

    public Restaurant findById(Long id) {
        return restaurantRepository.findById(id).orElse(null);
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
                ImageModel img = imageBusiness.getProfileImageByOwnerId(address.getRestaurant().getId(), true);
                List<ImageModel> lstImages = imageBusiness.getRestaurantImagesByOwner(address.getRestaurant().getId());
                List<ICurrentPeopleModel> currentPeople = reserveRepository
                        .findCurrentPeopleModelByRestaurant(restaurant.getId(), LocalDate.now());
                List<RestaurantDateTimeModel> dateTime = getListRestaurantDateTimeModel(
                        restaurantDateTimeRepository.findByRestaurant(restaurant));
                RestaurantModel model = RestaurantModel.builder().id(restaurant.getId())
                        .email(restaurant.getCredential().getEmail()).profileImage(img).cnpj(restaurant.getCnpj())
                        .cleaning(restaurant.getCleaning()).address(getAddressModelFromAddress(address))
                        .restaurantName(restaurant.getRestaurantName())
                        .currentPeople(getListCurrentPeopleModel(currentPeople)).restaurantImages(lstImages)
                        .maxNumberOfPeople(restaurant.getMaxNumberOfPeople()).averageStars(restaurant.getAverageStars())
                        .payment(restaurant.getPayment()).phoneNumber(restaurant.getPhoneNumber())
                        .description(restaurant.getDescription()).restaurantDateTime(dateTime).build();

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
    // DELETE
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void deleteRestaurant(Long id) {
        restaurantRepository.findById(id).map(r -> {
            Long idCred = r.getCredential().getId();
            imageBusiness.deleteImageByOwnerId(id);
            addressRepository.delete(addressRepository.findByRestaurant(r));
            restaurantDateTimeRepository.findByRestaurant(r).forEach(a -> restaurantDateTimeRepository.delete(a));
            restaurantRepository.delete(r);
            credentialBusiness.deleteCredentialById(idCred);
            return Void.TYPE;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.RESTAURANT_NOT_FOUND.getEnumValue()));
    }

    public void deleteImage(Long id) {
        imageBusiness.deleteImageById(id);
    }
}
