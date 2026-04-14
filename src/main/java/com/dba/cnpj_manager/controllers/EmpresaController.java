package com.dba.cnpj_manager.controllers;

import com.dba.cnpj_manager.dto.create.EmpresaCreateDTO;
import com.dba.cnpj_manager.dto.update.EmpresaUpdateDTO;
import com.dba.cnpj_manager.dto.response.EmpresaResponseDTO;
import com.dba.cnpj_manager.dto.response.FilialResponseDTO;
import com.dba.cnpj_manager.models.Empresa;
import com.dba.cnpj_manager.services.EmpresaService;
import com.dba.cnpj_manager.services.FilialService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    private final FilialService filialService;

    public EmpresaController(EmpresaService empresaService, FilialService filialService) {
        this.empresaService = empresaService;
        this.filialService = filialService;
    }

    @PostMapping
    @Operation(summary = "Criar empresa", description = "Cadastra uma nova empresa e retorna os dados salvos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empresa criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos campos (MethodArgumentNotValidException)."),
            @ApiResponse(responseCode = "422", description = "Erro de regra de negócio (BusinessValidationException).")
    })
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
    @Operation(summary = "Buscar por ID", description = "Retorna os detalhes de uma empresa específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa encontrada."),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada (ResourceNotFoundException).")
    })
    public ResponseEntity<EmpresaResponseDTO> buscarPorId(@PathVariable UUID id) {
        Empresa empresa = empresaService.buscarPorId(id);
        return ResponseEntity.ok(EmpresaResponseDTO.fromEntity(empresa));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar empresa", description = "Atualiza parcialmente os dados de uma empresa existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos campos."),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada."),
            @ApiResponse(responseCode = "422", description = "Erro de regra de negócio.")
    })
    public ResponseEntity<EmpresaResponseDTO> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody EmpresaUpdateDTO request) {
        Empresa empresaAtualizada = empresaService.atualizar(id, request);
        return ResponseEntity.ok(EmpresaResponseDTO.fromEntity(empresaAtualizada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar empresa", description = "Remove uma empresa e suas filhas baseado no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empresa removida com sucesso."),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada.")
    })
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        empresaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Listar todas", description = "Retorna uma lista de todas as empresas cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso.")
    })
    public ResponseEntity<List<EmpresaResponseDTO>> listarTodas() {
        List<EmpresaResponseDTO> empresas = empresaService.listarTodas();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}/filiais")
    @Operation(summary = "Listar filiais", description = "Retorna todas as filiais vinculadas a uma empresa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de filiais retornada."),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada.")
    })
    public ResponseEntity<List<FilialResponseDTO>> listarFiliais(@PathVariable UUID id) {
        List<FilialResponseDTO> filiais = filialService.listarPorEmpresa(id);
        return ResponseEntity.ok(filiais);
    }
}