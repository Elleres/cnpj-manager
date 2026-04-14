package com.dba.cnpj_manager.controllers;

import com.dba.cnpj_manager.dto.response.EnderecoResponseDTO;
import com.dba.cnpj_manager.dto.update.EnderecoUpdateDTO;
import com.dba.cnpj_manager.models.Endereco;
import com.dba.cnpj_manager.services.EnderecoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Buscar endereço por ID", description = "Retorna os detalhes de um endereço específico através do seu UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço encontrado."),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado.")
    })
    public ResponseEntity<EnderecoResponseDTO> buscarPorId(@PathVariable UUID id) {
        Endereco endereco = enderecoService.buscarPorId(id);
        return ResponseEntity.ok(EnderecoResponseDTO.fromEntity(endereco));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar endereço", description = "Atualiza parcialmente os dados de um endereço, incluindo coordenadas geográficas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos campos."),
            @ApiResponse(description = "Endereço não encontrado.", responseCode = "404"),
            @ApiResponse(responseCode = "422", description = "Erro de regra de negócio.")
    })
    public ResponseEntity<EnderecoResponseDTO> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody EnderecoUpdateDTO request) {

        // O Service lida com a conversão de lat/lng para o tipo geográfico do banco
        Endereco enderecoAtualizado = enderecoService.atualizar(id, request);

        return ResponseEntity.ok(EnderecoResponseDTO.fromEntity(enderecoAtualizado));
    }
}