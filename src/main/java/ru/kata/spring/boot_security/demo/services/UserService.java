package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {


    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }



    @Transactional
    public User findUserBYId(Integer id) {
        Optional<User> userfromDB  = userRepository.findById(id);
        return userfromDB.orElse(new User());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }
    @Transactional
    public boolean saveUser(User user) {
        Optional<User> userfromDB = userRepository.findByUsername(user.getUsername());

        if(!userfromDB.isEmpty()) {
            return false;
        }


        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }
    @Transactional
    public boolean deleteUser(Integer userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateUser(Integer userId, User user) {
        if(userRepository.findById(userId).isPresent()) {
            User userforUpdate = userRepository.getById(userId);
            userforUpdate.setUsername(user.getUsername());
            userforUpdate.setPassword(user.getPassword());
            userforUpdate.setEmail(user.getEmail());
            return true;
        }
        return false;
    }

    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }


    @Transactional
    public  Collection<Role> listRoles() {
        roleRepository.findAll();
        return roleRepository.findAll();
    }

}
