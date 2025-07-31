package com.eduardoxduardo.vlibrary.config;

import com.eduardoxduardo.vlibrary.model.entities.Role;
import com.eduardoxduardo.vlibrary.model.entities.User;
import com.eduardoxduardo.vlibrary.repository.RoleRepository;
import com.eduardoxduardo.vlibrary.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
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
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(adminRole, userRole));
            userRepository.save(admin);
        }
    }
}
