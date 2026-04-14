package com.dba.cnpj_manager.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dba.cnpj_manager.exceptions.TokenGenerationException;
import com.dba.cnpj_manager.models.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret:chave_secreta_padrao}")
    private String segredo;

    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(segredo);
            return JWT.create()
                    .withIssuer("cnpj-manager-api")
                    .withSubject(usuario.getNomeUsuario())
                    .withClaim("usuarioId", usuario.getId().toString())
                    .withClaim("perfil", usuario.getPerfil().name())
                    .withExpiresAt(gerarDataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException excecao) {
            throw new TokenGenerationException("Erro interno ao processar a assinatura de segurança da API.", excecao);
        }
    }

    public String validarToken(String token) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(segredo);
            return JWT.require(algoritmo)
                    .withIssuer("cnpj-manager-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException excecao) {
            return "";
        }
    }

    private Instant gerarDataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}