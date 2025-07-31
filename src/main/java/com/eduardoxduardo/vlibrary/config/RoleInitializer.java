package com.eduardoxduardo.vlibrary.config;

import com.eduardoxduardo.vlibrary.model.entities.Role;
import com.eduardoxduardo.vlibrary.model.entities.User;
import com.eduardoxduardo.vlibrary.repository.RoleRepository;
import com.eduardoxduardo.vlibrary.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RoleInitializer {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String ADMIN_DEFAULT_PASSWORD;

    public RoleInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, @Value("${ADMIN_DEFAULT_PASSWORD:}") String adminDefaultPassword) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.ADMIN_DEFAULT_PASSWORD = System.getenv("ADMIN_DEFAULT_PASSWORD");
    }

    @PostConstruct
    public void initRolesAndAdmin() {
        List<String> defaultRoles = List.of("ROLE_ADMIN", "ROLE_USER", "ROLE_LIBRARIAN");
        for (String roleName : defaultRoles) {
            roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(null, roleName, roleName + " system role", null)));
        }

        // Create the default admin if it doesn't exist
        if (userRepository.findByUsername("admin").isEmpty()) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
            Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow();
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@vlibrary.com");
            String defaultPassword = ADMIN_DEFAULT_PASSWORD;
            if (defaultPassword == null || defaultPassword.isBlank()) {
                throw new IllegalStateException("Environment variable ADMIN_DEFAULT_PASSWORD is not set or is empty. Please set a secure default password.");
            }
            admin.setPassword(passwordEncoder.encode(defaultPassword));
            admin.setRoles(Set.of(adminRole, userRole));
            userRepository.save(admin);
        }
    }
}
