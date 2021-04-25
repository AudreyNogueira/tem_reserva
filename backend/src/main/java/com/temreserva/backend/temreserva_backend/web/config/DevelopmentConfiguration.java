package com.temreserva.backend.temreserva_backend.web.config;

import com.temreserva.backend.temreserva_backend.data.repository.CredentialRepository;
import com.temreserva.backend.temreserva_backend.data.repository.RestaurantRepository;
import com.temreserva.backend.temreserva_backend.data.repository.SegmentRepository;
import com.temreserva.backend.temreserva_backend.data.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import com.temreserva.backend.temreserva_backend.data.entity.Credential;
import com.temreserva.backend.temreserva_backend.data.entity.Segment;
import com.temreserva.backend.temreserva_backend.data.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("development")
public class DevelopmentConfiguration {

    // INSERÇÕES PARA BANCO EM MEMÓRIA
    @Bean
    public CommandLineRunner createDataForTesting(@Autowired SegmentRepository segmentRepository,
            @Autowired UserRepository userRepository, @Autowired RestaurantRepository restaurantRepository,
            @Autowired CredentialRepository credentialRepository) {
        return args -> {
            System.out.println("Ambiente de desenvolvimento...");

            List<Segment> lstSegment = new ArrayList<Segment>();
            lstSegment.add(Segment.builder().description("Japonês").build());
            lstSegment.add(Segment.builder().description("Mexicana").build());
            lstSegment.add(Segment.builder().description("Italiana").build());
            segmentRepository.saveAll(lstSegment);

            Credential cred1 = Credential.builder().email("teste@email.com").password("123").build();
            cred1 = credentialRepository.save(cred1);
            User user1 = User.builder().credential(cred1).cpf("53415927865").name("teste teste").build();
            userRepository.save(user1);
        };
    }
}
