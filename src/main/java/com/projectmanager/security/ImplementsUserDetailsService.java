package com.projectmanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.projectmanager.model.Usuario;
import com.projectmanager.repository.UsuarioRepository;


@Service
public class ImplementsUserDetailsService implements UserDetailsService{

	@Autowired
	private UsuarioRepository crudUsuario;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Usuario usuario = crudUsuario.findByEmail(email.toUpperCase());
		
		if(usuario == null){
			throw new UsernameNotFoundException("Usuario não encontrado!");
		}
		
		return new User(usuario.getUsername(), usuario.getPassword(), true, true, true, true, usuario.getAuthorities());
	}

}
