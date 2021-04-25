package com.temreserva.backend.temreserva_backend.web.model;

import lombok.Getter;

public class ApiErrors {
    @Getter
    private String apicode;

    public ApiErrors(String apiCode) {
        this.apicode = apiCode;
    }
}
