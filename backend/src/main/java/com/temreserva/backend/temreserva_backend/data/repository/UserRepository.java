package com.temreserva.backend.temreserva_backend.data.repository;

import java.util.Optional;

import com.temreserva.backend.temreserva_backend.data.entity.Credential;
import com.temreserva.backend.temreserva_backend.data.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByCpf(String cpf);
    public Optional<User> findByCredential(Credential credential);
}
