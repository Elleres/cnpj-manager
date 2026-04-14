package com.dba.cnpj_manager.dto.response;

import com.dba.cnpj_manager.models.Filial;
import com.dba.cnpj_manager.models.Filial.TipoFilial;
import java.util.UUID;

public record FilialResponseDTO(
        UUID id,
        UUID empresaId,
        String nomeFantasia,
        String cnpjCompleto,
        TipoFilial tipo,
        Boolean ativa,
        EnderecoResponseDTO endereco) {
    public static FilialResponseDTO fromEntity(Filial filial) {
        return new FilialResponseDTO(
                filial.getId(),
                filial.getEmpresa().getId(),
                filial.getNomeFantasia(),
                filial.getCnpjCompleto(),
                filial.getTipo(),
                filial.isAtiva(),
                EnderecoResponseDTO.fromEntity(filial.getEndereco()));
    }
}