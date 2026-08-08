package com.speedtype.service;

import com.speedtype.model.User;
import com.speedtype.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/** Bridges our User entity to Spring Security's UserDetails contract. Fully
 *  qualifying org.springframework.security.core.userdetails.User below avoids a
 *  name clash with our own model.User.
 *
 *  The authority is "ROLE_" + the user's role (e.g. "ROLE_ADMIN") — Spring
 *  Security's hasRole("ADMIN") checks specifically look for that "ROLE_" prefix,
 *  so it has to be added here even though our own Role enum doesn't have it. */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
