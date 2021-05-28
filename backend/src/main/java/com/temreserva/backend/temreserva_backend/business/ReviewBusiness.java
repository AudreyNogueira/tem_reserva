package com.temreserva.backend.temreserva_backend.business;

import com.temreserva.backend.temreserva_backend.data.entity.Reserve;
import com.temreserva.backend.temreserva_backend.data.entity.Restaurant;
import com.temreserva.backend.temreserva_backend.data.entity.Review;
import com.temreserva.backend.temreserva_backend.data.entity.User;
import com.temreserva.backend.temreserva_backend.data.repository.ReviewRepository;
import com.temreserva.backend.temreserva_backend.web.model.dto.RestaurantDTO;
import com.temreserva.backend.temreserva_backend.web.model.dto.ReviewDTO;
import com.temreserva.backend.temreserva_backend.web.utils.Enumerators;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ReviewBusiness {
    RestaurantBusiness restaurantBusiness;
    ReserveBusiness reserveBusiness;
    UserBusiness userBusiness;
    ReviewRepository reviewRepository;

    public ReviewBusiness(RestaurantBusiness restaurantBusiness, ReserveBusiness reserveBusiness,
            UserBusiness userBusiness, ReviewRepository reviewRepository) {
        this.restaurantBusiness = restaurantBusiness;
        this.reserveBusiness = reserveBusiness;
        this.userBusiness = userBusiness;
        this.reviewRepository = reviewRepository;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // BUSINESS
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public HttpStatus saveReview(ReviewDTO dto) {
        Review review = validateDTO(dto);

        if (review != null) {
            reviewRepository.save(review);
            updateRestaurantStars(review.getRestaurant());
            return HttpStatus.CREATED;
        }

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                Enumerators.apiExceptionCodeEnum.INVALID_REVIEW.getEnumValue());
    }

    public void deleteReview(Long id) {
        reviewRepository.findById(id).map(r -> {
            Restaurant restaurant = r.getRestaurant();
            reviewRepository.delete(r);
            updateRestaurantStars(restaurant);
            return Void.TYPE;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.UNEXISTENT_REVIEW.getEnumValue()));
    }

    private void updateRestaurantStars(Restaurant restaurant) {
        restaurantBusiness.updateRestaurant(restaurant.getId(), RestaurantDTO.builder()
                .averageStars(reviewRepository.getUpdatedAverageStars(restaurant.getId())).build());
    }

    private Review validateDTO(ReviewDTO dto) {
        if (dto.getTitle() != null && dto.getStars() != null && dto.getIdRestaurant() != null && dto.getIdUser() != null
                && dto.getIdReserve() != null) {

            Reserve reserve = reserveBusiness.findById(dto.getIdReserve());
            Restaurant restaurant = restaurantBusiness.findById(dto.getIdRestaurant());
            User user = userBusiness.findById(dto.getIdUser());

            if (user != null && restaurant != null && reserve != null) {
                if (reviewRepository.existsByReserveUserRestaurant(reserve.getId(), user.getId(),
                        restaurant.getId()) == null)
                    return buildReserve(dto, reserve, restaurant, user);
                else
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            Enumerators.apiExceptionCodeEnum.REVIEW_EXISTS.getEnumValue());
            }
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.INVALID_REVIEW.getEnumValue());
    }

    public Review buildReserve(ReviewDTO dto, Reserve reserve, Restaurant restaurant, User user) {
        return Review.builder().reserve(reserve).user(user).restaurant(restaurant).description(dto.getDescription())
                .stars(dto.getStars()).title(dto.getTitle()).build();
    }
}
