package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;

import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class UserServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;




    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user =  userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.get().getUsername(),
                user.get().getPassword(),mapRolesAuthorities(user.get().getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesAuthorities(Collection<Role> roles) {
        return roles.stream().map(role ->new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }


}
