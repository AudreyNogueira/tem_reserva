package com.temreserva.backend.temreserva_backend.business;

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

    public void deleteReserve(Long id) {
        reserveRepository.deleteById(id);
    }

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
}
