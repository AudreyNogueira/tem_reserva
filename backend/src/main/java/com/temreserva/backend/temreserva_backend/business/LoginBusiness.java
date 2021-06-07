package com.temreserva.backend.temreserva_backend.business;

import com.temreserva.backend.temreserva_backend.data.entity.Credential;
import com.temreserva.backend.temreserva_backend.data.entity.Restaurant;
import com.temreserva.backend.temreserva_backend.data.entity.User;
import com.temreserva.backend.temreserva_backend.web.utils.Enumerators;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.temreserva.backend.temreserva_backend.web.model.dto.LoginDTO;
import com.temreserva.backend.temreserva_backend.web.model.response.LoginModel;
import com.temreserva.backend.temreserva_backend.web.model.response.RestaurantModel;
import com.temreserva.backend.temreserva_backend.web.model.response.UserModel;

public class LoginBusiness {
    public final OAuthBusiness oauthBusiness;
    private final CredentialBusiness credentialBusiness;
    private final RestaurantBusiness restaurantBusiness;
    private final UserBusiness userBusiness;

    public LoginBusiness(CredentialBusiness credentialBusiness, RestaurantBusiness restaurantBusiness,
            UserBusiness userBusiness) {
        oauthBusiness = new OAuthBusiness();
        this.credentialBusiness = credentialBusiness;
        this.userBusiness = userBusiness;
        this.restaurantBusiness = restaurantBusiness;
    }

    public LoginModel login(LoginDTO login, String authorization, String contentType) {            
             String username = login.getUser();
             String password = login.getPassword();
             String type = login.getLoginType();
             String accessToken = oauthBusiness.getAcessToken(username, password, authorization, contentType);

            if (accessToken != null) {
                Boolean isUser = type.equals("user");
                Credential credentials = credentialBusiness.getCredentialByEmail(username);

                return doLogin(isUser, credentials, accessToken);
            }

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    Enumerators.apiExceptionCodeEnum.USERNAME_OR_PASSWORD_INVALID.getEnumValue());
    }

    private LoginModel doLogin(Boolean isUser, Credential credentials, String accessToken) {
        LoginModel response;

        if (isUser)
            response = LoginModel.builder().accessToken(accessToken).user(userLogin(credentials)).build();
        else
            response = LoginModel.builder().accessToken(accessToken).restaurant(restaurantLogin(credentials)).build();

        return response;
    }

    private RestaurantModel restaurantLogin(Credential credentials) {
        Restaurant restaurantEntity = restaurantBusiness.findByCredential(credentials);

        if (restaurantEntity != null)
            return restaurantBusiness.getRestaurantById(restaurantEntity.getId());

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.RESTAURANT_NOT_FOUND.getEnumValue());
    }

    private UserModel userLogin(Credential credentials) {
        User userEntity = userBusiness.findByCredential(credentials);

        if (userEntity != null)
        return userBusiness.getUserById(userEntity.getId());

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.UNEXISTENT_USER.getEnumValue());
    }
}
