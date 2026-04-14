package com.dba.cnpj_manager.services;

import com.dba.cnpj_manager.dto.update.EnderecoUpdateDTO;
import com.dba.cnpj_manager.exceptions.ResourceNotFoundException;
import com.dba.cnpj_manager.models.Endereco;
import com.dba.cnpj_manager.repositories.EnderecoRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.UUID;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    @Transactional(readOnly = true)
    public Endereco buscarPorId(UUID id) {
        return findByIdOrThrow(id);
    }

    @Transactional
    public Endereco atualizar(UUID id, EnderecoUpdateDTO dto) {
        Endereco enderecoExistente = findByIdOrThrow(id);
        Objects.requireNonNull(enderecoExistente, "O endereço não deveria ser nulo aqui");

        if (dto.logradouro() != null)
            enderecoExistente.setLogradouro(dto.logradouro());
        if (dto.numero() != null)
            enderecoExistente.setNumero(dto.numero());
        if (dto.cidade() != null)
            enderecoExistente.setCidade(dto.cidade());
        if (dto.estado() != null)
            enderecoExistente.setEstado(dto.estado());
        if (dto.cep() != null)
            enderecoExistente.setCep(dto.cep());

        // 2. Recálculo da Geometria (JTS) se as coordenadas forem atualizadas
        if (dto.latitude() != null && dto.longitude() != null) {
            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
            enderecoExistente.setGeom(geometryFactory.createPoint(new Coordinate(dto.longitude(), dto.latitude())));

            enderecoExistente.setLatitude(dto.latitude());
            enderecoExistente.setLongitude(dto.longitude());
        }

        return enderecoRepository.save(enderecoExistente);
    }


    // --- Método Privado Seguro ---
    @NonNull
    private Endereco findByIdOrThrow(UUID id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com o ID: " + id));
    }
}