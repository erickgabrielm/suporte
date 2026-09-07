package com.senai.suporte.suporte.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senai.suporte.suporte.exception.RecursoNaoEncontradoException;
import com.senai.suporte.suporte.model.PainelTecnico;
import com.senai.suporte.suporte.model.Solicitacao;
import com.senai.suporte.suporte.model.Solicitacao.StatusSolicitacao;
import com.senai.suporte.suporte.repository.PainelTecnicoRepository;
import com.senai.suporte.suporte.repository.SolicitacaoRepository;

@Service
public class PainelTecnicoService {
		private final PainelTecnicoRepository painelTecnicoRepository;
		private final SolicitacaoRepository solicitacaoRepository;
		
		@Autowired
		public PainelTecnicoService(PainelTecnicoRepository painelTecnicoRepository,
				SolicitacaoRepository solicitacaoRepository) {
			this.painelTecnicoRepository = painelTecnicoRepository;
			this.solicitacaoRepository = solicitacaoRepository;
		}
		
		@Transactional
		public PainelTecnico assumir(Long solicitacaoID, String tecnicoResponsavel, String observacoes) {
			Solicitacao solicitacao = solicitacaoRepository.findById(solicitacaoID)
					.orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação", solicitacaoID));
			if(solicitacao.getStatus() != StatusSolicitacao.PENDENTE) {
				throw new IllegalStateException("Está solicitação não pode ser assumida, status atual: "
												+solicitacao.getStatus().getDescricao());
			}
			PainelTecnico painel = new PainelTecnico();
			painel.setSolicitacao(solicitacao);
			painel.setTecnicoResponsavel(tecnicoResponsavel);
			painel.setObservacoes(observacoes);
			
			solicitacao.setStatus(StatusSolicitacao.EM_ANDAMENTO);
			solicitacaoRepository.save(solicitacao);
			return painelTecnicoRepository.save(painel);
		}
		
		@Transactional 
		public void concluir(Long solicitacaoID) {
				Solicitacao solicitacao = solicitacaoRepository.findById(solicitacaoID)
						.orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação", solicitacaoID));
				if(solicitacao.getStatus() != StatusSolicitacao.PENDENTE) {
					throw new IllegalStateException("Está solicitação não pode ser concluida, status atual: "
													+solicitacao.getStatus().getDescricao());
				}
			solicitacao.setStatus(StatusSolicitacao.CONCLUIDO);
			solicitacaoRepository.save(solicitacao);
		}
		
		@Transactional(readOnly = true)
		public Optional<PainelTecnico> buscarPorSolicitacao(Long solicitacaoId){
			return painelTecnicoRepository.findById(solicitacaoId);
		}
}