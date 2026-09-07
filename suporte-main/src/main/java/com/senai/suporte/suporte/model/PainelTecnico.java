package com.senai.suporte.suporte.model;

import java.util.Objects;

import org.springframework.context.annotation.EnableAspectJAutoProxy;

import jakarta.annotation.Generated;
import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "painel_tecnico")
public class PainelTecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "solicitacao_id", nullable = false, unique = true)
    private Solicitacao solicitacao;

    @NotBlank(message = "Nome do tecnico é obragatório")
    @Column(nullable = false)
    private String tecnicoResponsavel;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

	/* ===================
	 * metodo construtor
	 =================*/

    public PainelTecnico() {

    }

    /* ===================
     * getters e setters
     =================*/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Solicitacao getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
    }

    public String getTecnicoResponsavel() {
        return tecnicoResponsavel;
    }

    public void setTecnicoResponsavel(String tecnicoResponsavel) {
        this.tecnicoResponsavel = tecnicoResponsavel;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    /*=============================================================
     * EQUALS() E hashCode() - igualdade baseada no id (padrão jpa)
     * =============================================================
     */

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass()!= o.getClass())
            return false;
        PainelTecnico that = (PainelTecnico) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass());
    }
}