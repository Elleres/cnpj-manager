package com.dba.cnpj_manager.controllers;

import com.dba.cnpj_manager.dto.request.LoginRequestDTO;
import com.dba.cnpj_manager.dto.response.TokenResponseDTO;
import com.dba.cnpj_manager.models.Usuario;
import com.dba.cnpj_manager.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> entrar(@RequestBody @Valid LoginRequestDTO requisicao) {
        var tokenAutenticacao = new UsernamePasswordAuthenticationToken(requisicao.nomeUsuario(), requisicao.senha());
        var autenticacao = authenticationManager.authenticate(tokenAutenticacao);

        var tokenJwt = tokenService.gerarToken((Usuario) autenticacao.getPrincipal());

        return ResponseEntity.ok(new TokenResponseDTO(tokenJwt, "Bearer"));
    }
}