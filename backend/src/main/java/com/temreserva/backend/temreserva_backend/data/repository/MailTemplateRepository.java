package com.temreserva.backend.temreserva_backend.data.repository;

import com.temreserva.backend.temreserva_backend.data.entity.MailTemplate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MailTemplateRepository extends JpaRepository<MailTemplate, Long> {
    @Query(value = "SELECT T.HTML FROM TB_TEMPLATE_EMAIL T WHERE T.ATIVO = TRUE AND T.DESCRICAO = :description", nativeQuery = true)
    public String findByDescription(@Param("description") String description);
}
