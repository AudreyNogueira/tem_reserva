package com.temreserva.backend.temreserva_backend.web.utils;

import com.temreserva.backend.temreserva_backend.web.model.ApiErrors;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.validation.FieldError;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getFieldError();
        String enumErrorName = fieldError.getObjectName().toUpperCase() + "_" + fieldError.getField().toUpperCase();
        return new ResponseEntity<>(
                new ApiErrors(Enumerators.apiExceptionCodeEnum.valueOf(enumErrorName).getEnumValue()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>(new ApiErrors(ex.getReason()), ex.getStatus());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return new ResponseEntity<>(new ApiErrors(Enumerators.apiExceptionCodeEnum.USERNAME_NOTFOUND.getEnumValue()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleDefaultException(Exception ex) {
        return new ResponseEntity<>(new ApiErrors(Enumerators.apiExceptionCodeEnum.DEFAULT_ERROR.getEnumValue()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidGrantException.class)
    public ResponseEntity<?> handleInvalidGrantException(InvalidGrantException ex) {
        return new ResponseEntity<>(
                new ApiErrors(Enumerators.apiExceptionCodeEnum.USERNAME_OR_PASSWORD_INVALID.getEnumValue()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OAuth2AccessDeniedException.class)
    public ResponseEntity<?> handleOAuth2AccessDeniedException(OAuth2AccessDeniedException ex) {
        return new ResponseEntity<>(
            new ApiErrors(Enumerators.apiExceptionCodeEnum.USERNAME_OR_PASSWORD_INVALID.getEnumValue()),
            HttpStatus.BAD_REQUEST);
    }        
}
