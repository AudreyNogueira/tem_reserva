package com.temreserva.backend.temreserva_backend.web.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDTO {
    private String user;
    private String password;
    private String loginType;
    private String grant_type;
}
