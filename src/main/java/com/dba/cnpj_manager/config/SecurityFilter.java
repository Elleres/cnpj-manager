package com.dba.cnpj_manager.config;

import com.dba.cnpj_manager.repositories.UsuarioRepository;
import com.dba.cnpj_manager.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public SecurityFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest requisicao, HttpServletResponse resposta,
            FilterChain cadeiaFiltros) throws ServletException, IOException {
        var token = this.recuperarToken(requisicao);

        if (token != null) {
            var login = tokenService.validarToken(token);

            if (!login.isEmpty()) {
                UserDetails usuario = usuarioRepository.findByNomeUsuario(login)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                var autenticacao = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(autenticacao);
            }
        }
        cadeiaFiltros.doFilter(requisicao, resposta);
    }

    private String recuperarToken(HttpServletRequest requisicao) {
        var cabecalhoAutorizacao = requisicao.getHeader("Authorization");
        if (cabecalhoAutorizacao == null || !cabecalhoAutorizacao.startsWith("Bearer ")) {
            return null;
        }
        return cabecalhoAutorizacao.replace("Bearer ", "");
    }
}