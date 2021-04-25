package com.temreserva.backend.temreserva_backend.business;

import com.temreserva.backend.temreserva_backend.data.entity.Credential;
import com.temreserva.backend.temreserva_backend.data.repository.CredentialRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CredentialBusiness implements UserDetailsService {

    private final CredentialRepository credentialRepository;

    public CredentialBusiness(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Credential credential = credentialRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Login incorreto ou inexistente"));

        return credential;
    }

    public Credential createNewCredential(String email, String password) {
        return credentialRepository.save(Credential.builder().email(email).password(password).build());
    }

    public Boolean validateNewCredential(String email, String password) {
        if(credentialRepository.findByEmail(email).orElse(null) != null)
            return false;
        
        return true;
    }

    public Credential getCredentialByEmail(String email) {
        return credentialRepository.findByEmail(email).orElse(null);
    }
}
