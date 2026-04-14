package com.dba.cnpj_manager.dto.response;

import java.util.UUID;

import com.dba.cnpj_manager.models.Empresa;

// Record simples para saída. O mapeamento da Entidade para este Record
// será feito no Controller ou em um componente de Mapper (ex: MapStruct).
public record EmpresaResponseDTO(
        UUID id,
        String razaoSocial,
        String nomeFantasia,
        String cnpjRaiz) {
    public static EmpresaResponseDTO fromEntity(Empresa empresa) {
        return new EmpresaResponseDTO(
                empresa.getId(),
                empresa.getRazaoSocial(),
                empresa.getNomeFantasia(),
                empresa.getCnpjCompleto());
    }
}