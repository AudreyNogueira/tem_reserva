package com.temreserva.backend.temreserva_backend.business;

import com.temreserva.backend.temreserva_backend.data.entity.Credential;
import com.temreserva.backend.temreserva_backend.data.repository.CredentialRepository;
import com.temreserva.backend.temreserva_backend.web.utils.Enumerators;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CredentialBusiness implements UserDetailsService {

    private final CredentialRepository credentialRepository;

    public CredentialBusiness(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // BUSINESS
    // ------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Credential credential = credentialRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Login incorreto ou inexistente"));

        return credential;
    }

    public Boolean validateNewCredential(String email, String password) {
        if (credentialRepository.findByEmail(email).orElse(null) != null)
            return false;

        return true;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // CREATE
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Credential createNewCredential(String email, String password) {
        return credentialRepository.save(Credential.builder().email(email).password(password).build());
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // READ
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Credential getCredentialByEmail(String email) {
        return credentialRepository.findByEmail(email).orElse(null);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // UPDATE
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void updateCredentialById(Long id, String email, String password) {
        credentialRepository.findById(id).map(c -> {
            c.setEmail(email);
            c.setPassword(password);
            return credentialRepository.save(c);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                Enumerators.apiExceptionCodeEnum.CREDENTIALS_NOT_FOUND.getEnumValue()));
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // DELETE
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void deleteCredentialById(Long id) {
        credentialRepository.findById(id).map(c -> {
            credentialRepository.delete(c);
            return Void.TYPE;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        Enumerators.apiExceptionCodeEnum.CREDENTIALS_NOT_FOUND.getEnumValue()));
    }
}
