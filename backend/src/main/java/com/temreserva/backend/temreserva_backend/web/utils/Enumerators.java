package com.temreserva.backend.temreserva_backend.web.utils;

public class Enumerators {

    public enum apiExceptionCodeEnum {
        DEFAULT_ERROR("0000"),
        CREATE_EXISTING_USER("0001"),
        USERDTO_CPF("0002"),
        UNEXISTENT_USER_TYPE("0003"),
        USERNAME_NOTFOUND("0004"),
        USERNAME_OR_PASSWORD_INVALID("0005"),
        RESTAURANT_NOT_FOUND("0006"),
        CREATE_RESTAURANT_UNAUTHORIZED("0007"),
        UPDATE_RESTAURANT_UNAUTHORIZED("0008"),
        UNEXISTENT_USER("0009"),
        CREATE_CREDENTIAL_ERROR("0010"),
        MISSING_RESERVE_INFO("0011"),
        BAD_RESERVE("0011"),
        CREDENTIALS_NOT_FOUND("0012"),
        WRONG_PASSWORD("0013"),
        IMAGE_NOT_FOUND("0014"),
        USER_HAVE_ACTIVE_RESERVE("0015"),
        FULL_RESTAURANT("0016")
        ;

        private final String apiCode;

        private apiExceptionCodeEnum(final String apiCode) {
            this.apiCode = apiCode;
        }

        public String getEnumValue() {
            return apiCode;
        }
    }
    
}
