package com.dba.cnpj_manager.dto.update;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoUpdateDTO(
        String logradouro,
        String numero,
        String cidade,

        @Size(min = 2, max = 2, message = "O estado deve conter 2 caracteres.") String estado,

        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter 8 dígitos.") String cep,

        Double latitude,
        Double longitude) {
}