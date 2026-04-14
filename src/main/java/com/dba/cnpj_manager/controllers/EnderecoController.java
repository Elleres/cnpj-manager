package com.dba.cnpj_manager.controllers;

import com.dba.cnpj_manager.dto.response.EnderecoResponseDTO;
import com.dba.cnpj_manager.dto.update.EnderecoUpdateDTO;
import com.dba.cnpj_manager.models.Endereco;
import com.dba.cnpj_manager.services.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> buscarPorId(@PathVariable UUID id) {
        Endereco endereco = enderecoService.buscarPorId(id);

        // Conversão limpa usando nosso Factory Method do DTO
        return ResponseEntity.ok(EnderecoResponseDTO.fromEntity(endereco));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody EnderecoUpdateDTO request) {

        // O Service já lida com a conversão das novas coordenadas (lat/lng)
        // para o objeto geográfico JTS Point (SDO_GEOMETRY do Oracle)
        Endereco enderecoAtualizado = enderecoService.atualizar(id, request);

        return ResponseEntity.ok(EnderecoResponseDTO.fromEntity(enderecoAtualizado));
    }
}