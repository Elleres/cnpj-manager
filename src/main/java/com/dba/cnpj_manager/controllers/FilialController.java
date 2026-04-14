package com.dba.cnpj_manager.controllers;

import com.dba.cnpj_manager.dto.create.FilialCreateDTO;
import com.dba.cnpj_manager.dto.response.FilialResponseDTO;
import com.dba.cnpj_manager.dto.update.FilialUpdateDTO;
import com.dba.cnpj_manager.models.Filial;
import com.dba.cnpj_manager.services.FilialService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/filiais")
public class FilialController {

    private final FilialService filialService;

    public FilialController(FilialService filialService) {
        this.filialService = filialService;
    }

    @PostMapping
    @Operation(summary = "Criar filial", description = "Cadastra uma nova filial vinculada a uma empresa matriz.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Filial criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos campos."),
            @ApiResponse(responseCode = "422", description = "Erro de regra de negócio.")
    })
    public ResponseEntity<FilialResponseDTO> criar(@Valid @RequestBody FilialCreateDTO request) {
        Filial filialSalva = filialService.criarFilial(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(filialSalva.getId())
                .toUri();

        return ResponseEntity.created(location).body(FilialResponseDTO.fromEntity(filialSalva));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar filial por ID", description = "Retorna os detalhes de uma filial específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filial encontrada."),
            @ApiResponse(responseCode = "404", description = "Filial não encontrada.")
    })
    public ResponseEntity<FilialResponseDTO> buscarPorId(@PathVariable UUID id) {
        Filial filial = filialService.buscarPorId(id);
        return ResponseEntity.ok(FilialResponseDTO.fromEntity(filial));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar filial", description = "Atualiza parcialmente os dados de uma filial existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filial atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos campos."),
            @ApiResponse(responseCode = "404", description = "Filial não encontrada."),
            @ApiResponse(responseCode = "422", description = "Erro de regra de negócio.")
    })
    public ResponseEntity<FilialResponseDTO> atualizar(@PathVariable UUID id,
            @Valid @RequestBody FilialUpdateDTO request) {
        Filial filialAtualizada = filialService.atualizar(id, request);
        return ResponseEntity.ok(FilialResponseDTO.fromEntity(filialAtualizada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar filial", description = "Remove uma filial do sistema baseado no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Filial removida com sucesso."),
            @ApiResponse(responseCode = "404", description = "Filial não encontrada.")
    })
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        filialService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}