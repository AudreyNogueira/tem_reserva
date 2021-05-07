package com.temreserva.backend.temreserva_backend.business;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import com.temreserva.backend.temreserva_backend.data.entity.Reserve;
import com.temreserva.backend.temreserva_backend.data.entity.Restaurant;
import com.temreserva.backend.temreserva_backend.data.entity.User;
import com.temreserva.backend.temreserva_backend.data.repository.MailTemplateRepository;
import com.temreserva.backend.temreserva_backend.data.repository.ReserveRepository;
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
    private final UserBusiness userBusiness;
    private final RestaurantBusiness restaurantBusiness;
    @Autowired
    private final MailBusiness mailBusiness;

    @Autowired
    public ReserveBusiness(ReserveRepository reserveRepository, RestaurantBusiness restaurantBusiness,
            UserBusiness userBusiness, MailTemplateRepository mailTemplateRepository) {
        this.reserveRepository = reserveRepository;
        this.restaurantBusiness = restaurantBusiness;
        this.userBusiness = userBusiness;
        this.mailBusiness = new MailBusiness(mailTemplateRepository);
    }
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // CREATE
    // ------------------------------------------------------------------------------------------------------------------------------------------

    public ReserveModel createNewReserve(@Valid ReserveDTO dto) throws MessagingException {
        Reserve reserve = validateNewReserve(dto);

        if (reserve != null) {
            reserveRepository.save(reserve);
            mailBusiness.sendMail(reserve.getUser().getCredential().getEmail(), reserve.getUser().getName().substring(0, reserve.getUser().getName().indexOf(" ")) + ", sua reserva foi feita com sucesso!", "reserve_success");
            return ReserveModel.builder().period(reserve.getPeriod()).reserveDate(reserve.getReserveDate())
                    .amountOfPeople(reserve.getAmountOfPeople()).id(reserve.getId())
                    .user(userBusiness.getUserById(reserve.getUser().getId()))
                    .restaurant(restaurantBusiness.getRestaurantById(reserve.getRestaurant().getId())).build();
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
        User user = userBusiness.findById(dto.getIdUser());
        Restaurant restaurant = restaurantBusiness.findById(dto.getIdRestaurant());
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
                        .user(userBusiness.getUserById(reserve.getUser().getId()))
                        .restaurant(restaurantBusiness.getRestaurantById(reserve.getRestaurant().getId())).build());
            } catch (Exception ex) {
                continue;
            }
        }

        return response;
    }

    public List<ReserveModel> getReservesByRestaurantId(Long restaurantId, LocalDateTime date) {
        Restaurant restaurant = restaurantBusiness.findById(restaurantId);
        List<Reserve> reserves = date != null ? reserveRepository.findByRestaurantCurrentDay(restaurantId, date)
                : reserveRepository.findByRestaurant(restaurant);
        return getReserveModelListByReserveList(reserves);
    }

    public List<ReserveModel> getReservesByUserId(Long userId, LocalDateTime date) {
        User user = userBusiness.findById(userId);
        List<Reserve> reserves = date != null ? reserveRepository.findByUserAndDate(userId, date)
                : reserveRepository.findByUser(user);
        return getReserveModelListByReserveList(reserves);
    }

    public ReserveModel getReserveById(Long id) {
        return reserveRepository.findById(id).map(reserve -> {
            return ReserveModel.builder().period(reserve.getPeriod()).reserveDate(reserve.getReserveDate())
                    .amountOfPeople(reserve.getAmountOfPeople()).id(reserve.getId())
                    .user(userBusiness.getUserById(reserve.getUser().getId()))
                    .restaurant(restaurantBusiness.getRestaurantById(reserve.getRestaurant().getId())).build();
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
