package com.dba.cnpj_manager.controllers;

import com.dba.cnpj_manager.dto.create.EmpresaCreateDTO;
import com.dba.cnpj_manager.dto.update.EmpresaUpdateDTO; // <-- Import do DTO de Update
import com.dba.cnpj_manager.dto.response.EmpresaResponseDTO;
import com.dba.cnpj_manager.models.Empresa;
import com.dba.cnpj_manager.services.EmpresaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> criar(@Valid @RequestBody EmpresaCreateDTO request) {
        Empresa empresaSalva = empresaService.criar(request);

        EmpresaResponseDTO response = EmpresaResponseDTO.fromEntity(empresaSalva);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(empresaSalva.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> buscarPorId(@PathVariable UUID id) {
        Empresa empresa = empresaService.buscarPorId(id);
        return ResponseEntity.ok(EmpresaResponseDTO.fromEntity(empresa));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody EmpresaUpdateDTO request) {

        Empresa empresaAtualizada = empresaService.atualizar(id, request);

        return ResponseEntity.ok(EmpresaResponseDTO.fromEntity(empresaAtualizada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma empresa baseada no id", description = "Remove uma empresa e suas entidades filhas baseado no id da empresa.")
    @ApiResponse(responseCode = "204", description = "Deletado com sucesso")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        empresaService.deletar(id);

        // Retorna 204 No Content (Sucesso, sem corpo na resposta)
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Listar todas as empresas", description = "Retorna uma lista de todas as empresas cadastradas no banco de dados.")
    @ApiResponse(responseCode = "200", description = "Lista de empresas recuperada com sucesso.")
    public ResponseEntity<List<EmpresaResponseDTO>> listarTodas() {
        List<EmpresaResponseDTO> empresas = empresaService.listarTodas();

        // Retorna 200 OK com a lista (mesmo que a lista esteja vazia [])
        return ResponseEntity.ok(empresas);
    }
}