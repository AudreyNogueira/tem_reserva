package com.temreserva.backend.temreserva_backend.business;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.temreserva.backend.temreserva_backend.data.entity.Reserve;
import com.temreserva.backend.temreserva_backend.data.entity.Restaurant;
import com.temreserva.backend.temreserva_backend.data.entity.User;
import com.temreserva.backend.temreserva_backend.data.repository.ReserveRepository;
import com.temreserva.backend.temreserva_backend.data.repository.RestaurantRepository;
import com.temreserva.backend.temreserva_backend.data.repository.UserRepository;
import com.temreserva.backend.temreserva_backend.web.model.DTOs.ReserveDTO;
import com.temreserva.backend.temreserva_backend.web.model.Responses.ReserveModel;
import com.temreserva.backend.temreserva_backend.web.utils.Enumerators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReserveBusiness {
    private final ReserveRepository reserveRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReserveBusiness(ReserveRepository reserveRepository, RestaurantRepository restaurantRepository,
            UserRepository userRepository) {
        this.reserveRepository = reserveRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // CREATE
    // ------------------------------------------------------------------------------------------------------------------------------------------
    
    public ReserveModel createNewReserve(@Valid ReserveDTO dto) {
        Reserve reserve = validateNewReserve(dto);

        if (reserve != null) {
            reserveRepository.save(reserve);
            return ReserveModel.builder().period(reserve.getPeriod()).reserveDate(reserve.getReserveDate())
                    .amountOfPeople(reserve.getAmountOfPeople()).id(reserve.getId()).idUser(reserve.getUser().getId())
                    .idRestaurant(reserve.getRestaurant().getId()).build();
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.BAD_RESERVE.getEnumValue());
    }
    
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // DELETE
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void deleteReserve(Long id) {
        reserveRepository.deleteById(id);
    }
    
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // BUSINESS
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private Reserve validateNewReserve(ReserveDTO dto) {
        User user = userRepository.findById(dto.getIdUser()).orElse(null);
        Restaurant restaurant = restaurantRepository.findById(dto.getIdRestaurant()).orElse(null);
        Reserve a = reserveRepository.existsByPeriodDateAndUser(dto.getIdUser(), dto.getPeriod(), dto.getReserveDate())
                .orElse(null);
        Integer b = reserveRepository.findNumberOfPeopleByRestaurantPeriodAndDate(dto.getIdRestaurant(),
                dto.getPeriod(), dto.getReserveDate());
        if (user != null && restaurant != null) {
            if (a == null) { // valida se usuário possui reserva no periodo
                if (b + dto.getAmountOfPeople() > restaurant.getMaxNumberOfPeople()) // valida total de pessoas do
                                                                                     // periodo
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            Enumerators.apiExceptionCodeEnum.FULL_RESTAURANT.getEnumValue());

                return Reserve.builder().period(dto.getPeriod()).amountOfPeople(dto.getAmountOfPeople()).user(user)
                        .restaurant(restaurant).reserveDate(dto.getReserveDate()).build();
            } else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        Enumerators.apiExceptionCodeEnum.USER_HAVE_ACTIVE_RESERVE.getEnumValue());
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.RESTAURANT_NOT_FOUND.getEnumValue());
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // GET
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public List<ReserveModel> getReserveModelListByReserveList(List<Reserve> reserves) {
        List<ReserveModel> response = new ArrayList<ReserveModel>();

        for (Reserve reserve : reserves) {
            try {
                response.add(ReserveModel.builder().period(reserve.getPeriod()).reserveDate(reserve.getReserveDate())
                        .amountOfPeople(reserve.getAmountOfPeople()).id(reserve.getId())
                        .idUser(reserve.getUser().getId()).idRestaurant(reserve.getRestaurant().getId()).build());
            } catch (Exception ex) {
                continue;
            }
        }

        return response;
    }

    public List<ReserveModel> getReservesByRestaurantId(Long restaurantId, LocalDateTime date) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        List<Reserve> reserves = date != null ? reserveRepository.findByRestaurantCurrentDay(restaurantId, date)
                : reserveRepository.findByRestaurant(restaurant);
        return getReserveModelListByReserveList(reserves);
    }

    public List<ReserveModel> getReservesByUserId(Long userId, LocalDateTime date) {
        User user = userRepository.findById(userId).orElse(null);
        List<Reserve> reserves = date != null ? reserveRepository.findByUserAndDate(userId, date)
                : reserveRepository.findByUser(user);
        return getReserveModelListByReserveList(reserves);
    }

    public ReserveModel getReserveById(Long id) {
        return reserveRepository.findById(id).map(reserve -> {
            return ReserveModel.builder().period(reserve.getPeriod()).reserveDate(reserve.getReserveDate())
                    .amountOfPeople(reserve.getAmountOfPeople()).id(reserve.getId()).idUser(reserve.getUser().getId())
                    .idRestaurant(reserve.getRestaurant().getId()).build();
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.RESERVE_NOT_FOUND.getEnumValue()));
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // UPDATE
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void updateReserve(Long id, ReserveDTO dto) {
        reserveRepository.findById(id).map(reserve -> {
            reserve.setAmountOfPeople(dto.getAmountOfPeople());
            reserve.setPeriod(dto.getPeriod());
            reserve.setReserveDate(dto.getReserveDate());
            return reserveRepository.save(reserve);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.RESERVE_NOT_FOUND.getEnumValue()));
    }
}
