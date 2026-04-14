package com.dba.cnpj_manager.dto.create;

import com.dba.cnpj_manager.models.Empresa;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

public record EmpresaCreateDTO(
                @NotBlank(message = "A razão social é obrigatória.") String razaoSocial,

                String nomeFantasia,

                @NotBlank(message = "O CNPJ é obrigatório.") @CNPJ(message = "CNPJ inválido. O CNPJ não passou na validação, verifique o tamanho ou o dígito verificador.") String cnpjCompleto) {

        public String cnpjLimpo() {
                if (this.cnpjCompleto == null)
                        return null;
                return this.cnpjCompleto.replaceAll("\\D", "");
        }

        public String cnpjRaiz() {
                String limpo = cnpjLimpo();
                if (limpo != null && limpo.length() >= 8) {
                        return limpo.substring(0, 8);
                }
                return limpo;
        }

        public Empresa toEntity() {
                Empresa empresa = new Empresa();
                empresa.setRazaoSocial(this.razaoSocial());
                empresa.setNomeFantasia(this.nomeFantasia());
                empresa.setCnpjCompleto(this.cnpjLimpo());

                return empresa;
        }
}