package com.dba.cnpj_manager.repositories;

import com.dba.cnpj_manager.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    // Método essencial para o filtro de segurança e o login
    Optional<Usuario> findByNomeUsuario(String nomeUsuario);
}