package com.dba.cnpj_manager.dto.create;

import com.dba.cnpj_manager.models.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

public record EnderecoCreateDTO(
        @NotBlank(message = "O logradouro é obrigatório.") String logradouro,

        String numero,

        @NotBlank(message = "A cidade é obrigatória.") String cidade,

        @NotBlank(message = "O estado é obrigatório.") @Size(min = 2, max = 2, message = "O estado deve conter exatamente 2 caracteres (Sigla).") String estado,

        @NotBlank(message = "O CEP é obrigatório.") @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 dígitos numéricos.") String cep,

        Double latitude,
        Double longitude) {
    public Endereco toEntity() {
        Endereco endereco = new Endereco();
        endereco.setLogradouro(this.logradouro());
        endereco.setNumero(this.numero());
        endereco.setCidade(this.cidade());
        endereco.setEstado(this.estado());
        endereco.setCep(this.cep());
        endereco.setLatitude(this.latitude());
        endereco.setLongitude(this.longitude());

        // Tratamento da coluna GEOMETRY usando JTS (Java Topology Suite)
        if (this.latitude() != null && this.longitude() != null) {
            // SRID 4326 é o padrão mundial para coordenadas GPS (WGS84)
            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

            endereco.setGeom(geometryFactory.createPoint(new Coordinate(this.longitude(), this.latitude())));
        }

        return endereco;
    }
}