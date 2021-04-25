package com.temreserva.backend.temreserva_backend.business;

import com.temreserva.backend.temreserva_backend.data.entity.Credential;
import com.temreserva.backend.temreserva_backend.data.entity.User;
import com.temreserva.backend.temreserva_backend.data.repository.UserRepository;
import com.temreserva.backend.temreserva_backend.web.model.DTOs.UserDTO;
import com.temreserva.backend.temreserva_backend.web.utils.Enumerators;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserBusiness {
    private final UserRepository userRepository;
    private final CredentialBusiness credentialBusiness;
    public final OAuthBusiness oauthBusiness;

    public UserBusiness(UserRepository userRepository, CredentialBusiness credentialBusiness) {
        this.userRepository = userRepository;
        this.credentialBusiness = credentialBusiness;
        oauthBusiness = new OAuthBusiness();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // BUSINESS
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public User userLogin(String parameters, String authorization, String contentType) {
        String username = parameters.substring(parameters.indexOf("=") + 1, parameters.indexOf("&")).replace("%40",
                "@");
        String password = parameters.substring(parameters.indexOf("&") + 10, parameters.indexOf("&grant_type"));
        String accessToken = oauthBusiness.getAcessToken(username, password, authorization, contentType);

        if (accessToken != null) {
            Credential userCredentials = credentialBusiness.getCredentialByEmail(username);
            User user = userRepository.findByCredential(userCredentials);
            user.setAccessToken(accessToken);
            user.getCredential().setPassword(null);
            return user;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.USERNAME_OR_PASSWORD_INVALID.getEnumValue());
    }

    public HttpStatus createNewUser(UserDTO dto) {
        User user = validateNewUserDto(dto);
        user.setCredential(credentialBusiness.createNewCredential(dto.getEmail(), dto.getPassword()));
        if (user.getCredential() != null) {
            userRepository.save(user);
            user.getCredential().setPassword(null);
            user.setAccessToken(oauthBusiness.getAcessToken(dto.getEmail(), dto.getPassword()));
            return HttpStatus.CREATED;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.CREATE_CREDENTIAL_ERROR.getEnumValue());
    }

    private User validateNewUserDto(UserDTO dto) {
        if (credentialBusiness.validateNewCredential(dto.getEmail(), dto.getPassword()))
            return new User(dto);
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    Enumerators.apiExceptionCodeEnum.CREATE_EXISTING_USER.getEnumValue());
    }
}
