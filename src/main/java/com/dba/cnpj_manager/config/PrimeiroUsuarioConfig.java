package com.dba.cnpj_manager.config;

import com.dba.cnpj_manager.models.Usuario;
import com.dba.cnpj_manager.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PrimeiroUsuarioConfig implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public PrimeiroUsuarioConfig(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${ADMIN_PASSWORD}")
    private String senhaAdmin;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setNomeUsuario("admin");
            admin.setEmail("admin@cnpjmanager.com");
            admin.setSenha(passwordEncoder.encode(senhaAdmin));
            admin.setPerfil(Usuario.Role.ADMIN);

            usuarioRepository.save(admin);
        }
    }
}