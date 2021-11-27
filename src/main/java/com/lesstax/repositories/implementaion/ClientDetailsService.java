/*package com.lesstax.repositories.implementaion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.lesstax.model.Client;
import com.lesstax.repositories.ClientRepository;

@Service
public class ClientDetailsService implements UserDetailsService {

	@Autowired
	private ClientRepository clientRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		Client client = clientRepository.findByEmail(username);
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = client.getPassword();
		String encodedPassword = passwordEncoder.encode(password);
		if (client == null) {
			throw new UsernameNotFoundException(username);
		}

		UserDetails user = User.withUsername(client.getEmail()).password(encodedPassword).authorities("USER")
				.build();
		return user;
	}
*/