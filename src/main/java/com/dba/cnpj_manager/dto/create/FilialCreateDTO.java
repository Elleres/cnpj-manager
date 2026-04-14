package com.dba.cnpj_manager.dto.create;

import com.dba.cnpj_manager.models.Empresa;
import com.dba.cnpj_manager.models.Endereco;
import com.dba.cnpj_manager.models.Filial;
import com.dba.cnpj_manager.models.Filial.TipoFilial;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

import java.util.UUID;

public record FilialCreateDTO(
        @NotNull(message = "O ID da empresa matriz é obrigatório.") UUID empresaId,

        @NotBlank(message = "O CNPJ é obrigatório.") @CNPJ(message = "O CNPJ fornecido é inválido.") String cnpjCompleto,

        @NotNull(message = "O tipo da filial é obrigatório.") TipoFilial tipo,

        @NotNull(message = "O status da filial (ativa/inativa) é obrigatório.") Boolean ativa,

        @NotNull(message = "Os dados de endereço são obrigatórios.") @Valid EnderecoCreateDTO endereco) {

    public String cnpjLimpo() {
        return this.cnpjCompleto == null ? null : this.cnpjCompleto.replaceAll("\\D", "");
    }

    public Filial toEntity(Empresa empresaVinculada) {
        Filial filial = new Filial();
        filial.setEmpresa(empresaVinculada);

        filial.setCnpjCompleto(this.cnpjLimpo());

        filial.setTipo(this.tipo());
        filial.setAtiva(this.ativa());

        if (this.endereco() != null) {
            Endereco enderecoEntity = this.endereco().toEntity();
            enderecoEntity.setFilial(filial);
            filial.setEndereco(enderecoEntity);
        }

        return filial;
    }
}