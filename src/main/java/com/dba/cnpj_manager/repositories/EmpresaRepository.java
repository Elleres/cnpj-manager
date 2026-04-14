package com.dba.cnpj_manager.repositories;

import com.dba.cnpj_manager.models.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {
    Optional<Empresa> findByCnpjCompleto(String cnpjRaiz);

    boolean existsByCnpjCompletoStartingWith(String cnpjRaiz);
}