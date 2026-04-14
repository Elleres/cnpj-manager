package com.dba.cnpj_manager.controllers;

import com.dba.cnpj_manager.dto.create.EmpresaCreateDTO;
import com.dba.cnpj_manager.dto.update.EmpresaUpdateDTO; // <-- Import do DTO de Update
import com.dba.cnpj_manager.dto.response.EmpresaResponseDTO;
import com.dba.cnpj_manager.models.Empresa;
import com.dba.cnpj_manager.services.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        empresaService.deletar(id);

        // Retorna 204 No Content (Sucesso, sem corpo na resposta)
        return ResponseEntity.noContent().build();
    }
}