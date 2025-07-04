package com.eduardoxduardo.vlibrary.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "library")
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "users")
public class User implements Serializable, UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<UserBook> library;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // For now, we assign a default role to all users.
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        // For now, we assume accounts are always non-expired.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // For now, we assume accounts are always non-locked.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // For now, we assume credentials are always valid.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // For now, we assume all users are enabled.
        return true;
    }
}
