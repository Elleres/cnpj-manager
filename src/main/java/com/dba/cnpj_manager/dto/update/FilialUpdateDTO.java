package com.dba.cnpj_manager.dto.update;

import com.dba.cnpj_manager.models.Filial.TipoFilial;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.br.CNPJ;

public record FilialUpdateDTO(
        @CNPJ(message = "O CNPJ fornecido é inválido.") String cnpjCompleto,

        TipoFilial tipo,
        Boolean ativa,

        @Valid EnderecoUpdateDTO endereco) {
    public String cnpjLimpo() {
        return (this.cnpjCompleto == null) ? null : this.cnpjCompleto.replaceAll("\\D", "");
    }

    public String cnpjRaiz() {
        String limpo = cnpjLimpo();
        return (limpo != null && limpo.length() >= 8) ? limpo.substring(0, 8) : limpo;
    }
}