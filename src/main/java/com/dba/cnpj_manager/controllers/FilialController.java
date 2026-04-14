package com.dba.cnpj_manager.controllers;

import com.dba.cnpj_manager.dto.create.FilialCreateDTO;
import com.dba.cnpj_manager.dto.response.FilialResponseDTO;
import com.dba.cnpj_manager.dto.update.FilialUpdateDTO;
import com.dba.cnpj_manager.models.Filial;
import com.dba.cnpj_manager.services.FilialService;
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
    public ResponseEntity<FilialResponseDTO> criar(@Valid @RequestBody FilialCreateDTO request) {
        Filial filialSalva = filialService.criarFilial(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(filialSalva.getId())
                .toUri();

        return ResponseEntity.created(location).body(FilialResponseDTO.fromEntity(filialSalva));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilialResponseDTO> buscarPorId(@PathVariable UUID id) {
        Filial filial = filialService.buscarPorId(id);
        return ResponseEntity.ok(FilialResponseDTO.fromEntity(filial));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FilialResponseDTO> atualizar(@PathVariable UUID id,
            @Valid @RequestBody FilialUpdateDTO request) {
        Filial filialAtualizada = filialService.atualizar(id, request);
        return ResponseEntity.ok(FilialResponseDTO.fromEntity(filialAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        filialService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}