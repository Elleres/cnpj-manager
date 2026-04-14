package com.dba.cnpj_manager.dto.update;

import org.hibernate.validator.constraints.br.CNPJ;

public record EmpresaUpdateDTO(
        String razaoSocial,
        String nomeFantasia,

        @CNPJ(message = "CNPJ inválido.") String cnpjCompleto) {
    public String cnpjLimpo() {
        return (this.cnpjCompleto == null) ? null : this.cnpjCompleto.replaceAll("\\D", "");
    }

    public String cnpjRaiz() {
        String limpo = cnpjLimpo();
        return (limpo != null && limpo.length() >= 8) ? limpo.substring(0, 8) : limpo;
    }
}