package com.dba.cnpj_manager.repositories;

import com.dba.cnpj_manager.models.Filial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilialRepository extends JpaRepository<Filial, UUID> {
    boolean existsByCnpjCompleto(String cnpjCompleto);

    Optional<Filial> findByCnpjCompleto(String cnpjCompleto);

    List<Filial> findByEmpresaId(UUID empresaId);
}