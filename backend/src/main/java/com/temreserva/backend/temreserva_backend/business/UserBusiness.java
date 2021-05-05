package com.temreserva.backend.temreserva_backend.business;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

import com.temreserva.backend.temreserva_backend.data.entity.Credential;
import com.temreserva.backend.temreserva_backend.data.entity.User;
import com.temreserva.backend.temreserva_backend.data.repository.UserRepository;
import com.temreserva.backend.temreserva_backend.web.model.DTOs.UserDTO;
import com.temreserva.backend.temreserva_backend.web.model.Responses.ImageModel;
import com.temreserva.backend.temreserva_backend.web.model.Responses.UserModel;
import com.temreserva.backend.temreserva_backend.web.utils.Enumerators;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class UserBusiness {
    private final UserRepository userRepository;
    private final CredentialBusiness credentialBusiness;
    private final ImageBusiness imageBusiness;
    private final OAuthBusiness oauthBusiness;

    public UserBusiness(UserRepository userRepository, ImageBusiness imageBusiness,
            CredentialBusiness credentialBusiness) {
        this.userRepository = userRepository;
        this.credentialBusiness = credentialBusiness;
        this.imageBusiness = imageBusiness;
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
            // user.setAccessToken(accessToken);
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
            // user.setAccessToken(oauthBusiness.getAcessToken(dto.getEmail(),
            // dto.getPassword()));
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

    @Transactional(rollbackOn = ResponseStatusException.class)
    public void updateUser(Long id, UserDTO user) {
        User userResponse = userRepository.findById(id).map(u -> {
            u.setName(user.getName() != null ? user.getName() : u.getName());
            u.setPhoneNumber(user.getPhoneNumber() != null ? user.getPhoneNumber() : u.getPhoneNumber());
            u.setCpf(user.getCpf() != null ? user.getCpf() : u.getCpf());
            u.setUpdateDate(LocalDateTime.now());
            u.setBirthDate(user.getBirthDate() != null ? user.getBirthDate() : u.getBirthDate());
            credentialBusiness.updateEmailByID(u.getCredential().getId(), user.getEmail());
            return userRepository.save(u);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                Enumerators.apiExceptionCodeEnum.UNEXISTENT_USER.getEnumValue()));

        if (user.getActualPassword() != null && user.getPassword() != null) {
            if (userResponse.getCredential().getPassword().equals(user.getActualPassword()))
                credentialBusiness.updatePasswordById(userResponse.getCredential().getId(), user.getPassword());
            else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        Enumerators.apiExceptionCodeEnum.WRONG_PASSWORD.getEnumValue());
        }
    }

    public ImageModel userImageUpload(MultipartFile file, Long id) throws IOException {
        if (userRepository.findById(id).orElse(null) != null)
            return imageBusiness.imageUpload(file, id, true, false);

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.UNEXISTENT_USER.getEnumValue());
    }

    public UserModel getUserById(Long id) {
        return userRepository.findById(id).map(u -> {
            return UserModel.builder().phoneNumber(u.getPhoneNumber()).id(id).email(u.getCredential().getEmail())
                    .cpf(u.getCpf()).birthDate(u.getBirthDate()).name(u.getName())
                    .image(imageBusiness.getProfileImageByOwnerId(id, false)).build();
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.UNEXISTENT_USER.getEnumValue()));
    }

    public void deleteUser(Long id) {
        userRepository.findById(id).map(u -> {
            Long idCred = u.getCredential().getId();
            imageBusiness.deleteImageByOwnerId(id);
            userRepository.delete(u);
            credentialBusiness.deleteCredentialById(idCred);
            return Void.TYPE;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Enumerators.apiExceptionCodeEnum.UNEXISTENT_USER.getEnumValue()));
    }
}
