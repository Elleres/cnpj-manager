package com.dba.cnpj_manager.models;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Entity
@Table(name = "filial")
@Getter
@Setter
public class Filial implements Serializable {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name = "cnpj_completo", length = 14, unique = true, nullable = false)
    private String cnpjCompleto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoFilial tipo;

    @Column(name = "ativa", nullable = false)
    private boolean ativa = true;

    @OneToOne(mappedBy = "filial", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Endereco endereco;

    public enum TipoFilial {
        MATRIZ, FILIAL
    }

}