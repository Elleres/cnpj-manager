package com.dba.cnpj_manager.dto.response;

import com.dba.cnpj_manager.models.Endereco;
import java.util.UUID;

public record EnderecoResponseDTO(
        UUID id,
        String logradouro,
        String numero,
        String cidade,
        String estado,
        String cep,
        Double latitude,
        Double longitude) {
    public static EnderecoResponseDTO fromEntity(Endereco endereco) {
        if (endereco == null)
            return null;

        return new EnderecoResponseDTO(
                endereco.getId(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep(),
                endereco.getLatitude(),
                endereco.getLongitude());
    }
}