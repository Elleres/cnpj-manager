package com.dba.cnpj_manager.services;

import com.dba.cnpj_manager.dto.create.EmpresaCreateDTO;
import com.dba.cnpj_manager.dto.response.EmpresaResponseDTO;
import com.dba.cnpj_manager.dto.response.FilialResponseDTO;
import com.dba.cnpj_manager.dto.update.EmpresaUpdateDTO;
import com.dba.cnpj_manager.exceptions.BusinessValidationException;
import com.dba.cnpj_manager.exceptions.ResourceNotFoundException;
import com.dba.cnpj_manager.models.Empresa;
import com.dba.cnpj_manager.repositories.EmpresaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.UUID;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Transactional
    public Empresa criar(EmpresaCreateDTO novaEmpresa) {
        BusinessValidationException ex = new BusinessValidationException("Erro ao cadastrar empresa.");

        // Valida usando a mesma lógica centralizada
        validarRegrasCnpj(null, novaEmpresa.cnpjLimpo(), novaEmpresa.cnpjRaiz(), ex);

        if (ex.hasErrors())
            throw ex;

        return empresaRepository.save(novaEmpresa.toEntity());
    }

    @Transactional(readOnly = true)
    public Empresa buscarPorId(UUID id) {
        return findByIdOrThrow(id);
    }

    @Transactional
    public Empresa atualizar(UUID idEmpresaExistente, EmpresaUpdateDTO novaEmpresa) {
        Empresa empresaExistente = findByIdOrThrow(idEmpresaExistente);
        BusinessValidationException ex = new BusinessValidationException("Erro ao atualizar os dados da empresa.");

        if (novaEmpresa.cnpjCompleto() != null) {
            // Chamada da nossa função organizada
            validarRegrasCnpj(empresaExistente, novaEmpresa.cnpjLimpo(), novaEmpresa.cnpjRaiz(), ex);

            if (!ex.hasErrors()) {
                empresaExistente.setCnpjCompleto(novaEmpresa.cnpjLimpo());
            }
        }

        if (ex.hasErrors())
            throw ex;

        if (novaEmpresa.razaoSocial() != null)
            empresaExistente.setRazaoSocial(novaEmpresa.razaoSocial());
        if (novaEmpresa.nomeFantasia() != null)
            empresaExistente.setNomeFantasia(novaEmpresa.nomeFantasia());

        return empresaRepository.save(empresaExistente);
    }

    @Transactional
    public void deletar(UUID id) {
        Empresa empresa = findByIdOrThrow(id);
        empresaRepository.delete(empresa);
    }

    public List<EmpresaResponseDTO> listarTodas() {
        List<Empresa> empresas = empresaRepository.findAll();

        // Mapeia a lista de Entidades do banco para uma lista de DTOs para o Front-end
        return empresas.stream()
                .map(empresa -> new EmpresaResponseDTO(
                        empresa.getId(),
                        empresa.getRazaoSocial(),
                        empresa.getNomeFantasia(),
                        empresa.getCnpjCompleto()))
                .toList();
    }

    // --- MÉTODOS PRIVADOS ---
    @NonNull
    private Empresa findByIdOrThrow(UUID id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com o ID: " + id));
    }

    private void validarRegrasCnpj(Empresa empresaExistente, String novoCnpjLimpo, String novaRaiz,
            BusinessValidationException ex) {

        String raizAtual = (empresaExistente != null) ? empresaExistente.getCnpjCompleto().substring(0, 8) : null;

        // Se é criação OU a raiz mudou
        if (empresaExistente == null || !novaRaiz.equals(raizAtual)) {

            // Se o CNPJ completo já existe
            if (empresaRepository.findByCnpjCompleto(novoCnpjLimpo).isPresent()) {
                ex.addError(BusinessValidationException.FIELD_CNPJ, "O CNPJ informado já está em uso.");
            }

            // Se já existe uma matriz usando o CNPJ raiz
            if (empresaRepository.existsByCnpjCompletoStartingWith(novaRaiz)) {
                ex.addError(BusinessValidationException.FIELD_CNPJ,
                        "Já existe uma empresa matriz cadastrada para esta família de CNPJ.");
            }
        }
    }
}