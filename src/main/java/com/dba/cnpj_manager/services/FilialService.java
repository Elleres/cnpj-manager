package com.dba.cnpj_manager.services;

import com.dba.cnpj_manager.dto.create.FilialCreateDTO;
import com.dba.cnpj_manager.dto.update.EnderecoUpdateDTO;
import com.dba.cnpj_manager.dto.update.FilialUpdateDTO;
import com.dba.cnpj_manager.exceptions.BusinessValidationException;
import com.dba.cnpj_manager.exceptions.ResourceNotFoundException;
import com.dba.cnpj_manager.models.Empresa;
import com.dba.cnpj_manager.models.Endereco;
import com.dba.cnpj_manager.models.Filial;
import com.dba.cnpj_manager.repositories.EmpresaRepository;
import com.dba.cnpj_manager.repositories.FilialRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.UUID;

@Service
public class FilialService {

    private final FilialRepository filialRepository;
    private final EmpresaRepository empresaRepository;

    public FilialService(FilialRepository filialRepository, EmpresaRepository empresaRepository) {
        this.filialRepository = filialRepository;
        this.empresaRepository = empresaRepository;
    }

    @Transactional
    public Filial criarFilial(FilialCreateDTO dto) {
        Empresa matriz = empresaRepository.findById(dto.empresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Matriz não encontrada."));

        BusinessValidationException ex = new BusinessValidationException("Erro ao cadastrar filial.");

        // Chama a validação centralizada passando "null" para a filial existente (pois
        // é criação)
        validarRegrasCnpj(null, dto.cnpjLimpo(), matriz, ex);

        if (ex.hasErrors()) {
            throw ex;
        }

        Filial filial = dto.toEntity(matriz);
        return filialRepository.save(filial);
    }

    @Transactional(readOnly = true)
    public Filial buscarPorId(UUID id) {
        return findByIdOrThrow(id);
    }

    @Transactional
    public Filial atualizar(UUID id, FilialUpdateDTO dto) {
        Filial filialExistente = findByIdOrThrow(id);
        Objects.requireNonNull(filialExistente, "A filial não deveria ser nula aqui");

        BusinessValidationException ex = new BusinessValidationException("Erro ao atualizar a filial.");

        if (dto.cnpjCompleto() != null) {
            validarRegrasCnpj(filialExistente, dto.cnpjLimpo(), filialExistente.getEmpresa(), ex);

            if (!ex.hasErrors()) {
                filialExistente.setCnpjCompleto(dto.cnpjLimpo());
            }
        }

        if (ex.hasErrors())
            throw ex;

        if (dto.tipo() != null)
            filialExistente.setTipo(dto.tipo());
        if (dto.ativa() != null)
            filialExistente.setAtiva(dto.ativa());

        if (dto.endereco() != null) {
            atualizarDadosEndereco(filialExistente.getEndereco(), dto.endereco());
        }

        return filialRepository.save(filialExistente);
    }

    @Transactional
    public void deletar(UUID id) {
        Filial filial = findByIdOrThrow(id);

        filialRepository.delete(filial);
    }

    // --- MÉTODOS PRIVADOS DE SUPORTE ---

    private void validarRegrasCnpj(Filial filialExistente, String novoCnpjLimpo, Empresa matriz,
            BusinessValidationException ex) {
        boolean cnpjMudouOuEhNovo = (filialExistente == null)
                || !novoCnpjLimpo.equals(filialExistente.getCnpjCompleto());

        if (cnpjMudouOuEhNovo) {
            if (filialRepository.existsByCnpjCompleto(novoCnpjLimpo)) {
                ex.addError(BusinessValidationException.FIELD_CNPJ, "Esta filial já está cadastrada.");
            }

            String raizNova = novoCnpjLimpo.substring(0, 8);
            String raizMatriz = matriz.getCnpjCompleto().substring(0, 8);

            if (!raizMatriz.equals(raizNova)) {
                ex.addError(BusinessValidationException.FIELD_CNPJ,
                        "A raiz deste CNPJ não pertence à matriz vinculada.");
            }
        }
    }

    @NonNull
    private Filial findByIdOrThrow(UUID id) {
        return filialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filial não encontrada com o ID: " + id));
    }

    private void atualizarDadosEndereco(Endereco enderecoAtual, EnderecoUpdateDTO novoEnd) {
        if (novoEnd.logradouro() != null)
            enderecoAtual.setLogradouro(novoEnd.logradouro());
        if (novoEnd.numero() != null)
            enderecoAtual.setNumero(novoEnd.numero());
        if (novoEnd.cidade() != null)
            enderecoAtual.setCidade(novoEnd.cidade());
        if (novoEnd.estado() != null)
            enderecoAtual.setEstado(novoEnd.estado());
        if (novoEnd.cep() != null)
            enderecoAtual.setCep(novoEnd.cep());

        // Recálculo da Geometria (JTS) se as coordenadas mudarem
        if (novoEnd.latitude() != null && novoEnd.longitude() != null) {
            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
            enderecoAtual.setGeom(geometryFactory.createPoint(new Coordinate(novoEnd.longitude(), novoEnd.latitude())));
            enderecoAtual.setLatitude(novoEnd.latitude());
            enderecoAtual.setLongitude(novoEnd.longitude());
        }
    }
}