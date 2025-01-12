package com.example.SpringSecurityApp.SpringSecurityApp.services;
import com.example.SpringSecurityApp.SpringSecurityApp.dto.SignUpDto;
import com.example.SpringSecurityApp.SpringSecurityApp.dto.UserDto;
import com.example.SpringSecurityApp.SpringSecurityApp.entities.User;
import com.example.SpringSecurityApp.SpringSecurityApp.exceptions.ResourceNotFoundException;
import com.example.SpringSecurityApp.SpringSecurityApp.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ModelMapper modelMapper,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " +
                        username + " not found."));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->  new ResourceNotFoundException("User not found with id: " + userId));
    }

    public UserDto signUp(SignUpDto signUpDto) {
        Optional<User> userOptional = userRepository.findByEmail(signUpDto.getEmail());
        if(userOptional.isPresent()) {
            throw new BadCredentialsException("User with email " + signUpDto.getEmail() + " already exists");
        }

        User userToCreate = modelMapper.map(signUpDto, User.class);
        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));

        User savedUser = userRepository.save(userToCreate);

        return modelMapper.map(savedUser, UserDto.class);
    }
}