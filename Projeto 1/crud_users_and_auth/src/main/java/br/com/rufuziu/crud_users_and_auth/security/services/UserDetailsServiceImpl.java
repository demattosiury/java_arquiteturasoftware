package br.com.rufuziu.crud_users_and_auth.security.services;

import br.com.rufuziu.crud_users_and_auth.entity.User;
import br.com.rufuziu.crud_users_and_auth.repository.UserRepository;
import br.com.rufuziu.crud_users_and_auth.security.dto.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String email) {
        Optional<User> user = repository.findByEmail(email);
        if (user.isPresent()) {
            return UserDetailsImpl.build(user.get());
        } else {
            throw new UsernameNotFoundException("Invalid email!");
        }
    }
}
