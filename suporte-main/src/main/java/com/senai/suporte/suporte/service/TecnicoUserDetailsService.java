package com.senai.suporte.suporte.service;

import java.util.List;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.senai.suporte.suporte.model.Tecnico;
import com.senai.suporte.suporte.repository.TecnicoRepository;

@Service
public class TecnicoUserDetailsService implements UserDetailsService {
	
	private final TecnicoRepository tecnicoRepository;

	public TecnicoUserDetailsService(TecnicoRepository tecnicoRepository) {
		this.tecnicoRepository = tecnicoRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		String loginLimpo = login.trim();
		
		List<Tecnico> encontrados = tecnicoRepository.buscarPorEmailOuNome(loginLimpo);
		if(encontrados.isEmpty()) {
			throw new UsernameNotFoundException("Tecnico não encontrado: " + loginLimpo);
		} 
		if(encontrados.size() > 1) {
			throw new UsernameNotFoundException("Há mais de um usuario com esse nome, Entre com o e-mail");
		}
		
		Tecnico tecnico = encontrados.get(0);
		return User.builder()
				.username(tecnico.getEmail())
				.password(tecnico.getSenha())
				.roles("TECNICO")
				.build();
	}
}