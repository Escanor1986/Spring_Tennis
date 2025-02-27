package com.escanor1986.tennis.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.escanor1986.tennis.data.RoleEntity;
import com.escanor1986.tennis.data.UserEntity;
import com.escanor1986.tennis.data.UserRepository;

@Component  // Indique à Spring de gérer cette classe comme un composant (bean)
public class EscanorUserDetailsService implements UserDetailsService {

    // Injection du repository qui permet d'accéder aux données utilisateur dans la base
    private final UserRepository userRepository;

    public EscanorUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Méthode principale de UserDetailsService. Spring Security l'appelle lors d'une tentative d'authentification.
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // On cherche l'utilisateur par son login (ici, en ignorant la casse)
        return userRepository.findOneWithRolesByLoginIgnoreCase(login)
            // Si l'utilisateur est trouvé, on le transforme en objet User de Spring Security
            .map(this::createSpringSecurityUser)
            // Sinon, on lève une exception pour indiquer que l'utilisateur n'existe pas.
            .orElseThrow(() -> new UsernameNotFoundException("User with login " + login + " could not be found"));
    }

    // Cette méthode convertit notre entité utilisateur (UserEntity) en un objet User propre à Spring Security.
    private User createSpringSecurityUser(UserEntity userEntity) {
        // On transforme la liste de rôles (par exemple "ROLE_ADMIN", "ROLE_USER") en une liste d'objets SimpleGrantedAuthority
        // Ces objets indiquent à Spring Security les permissions dont dispose l'utilisateur.
        List<SimpleGrantedAuthority> grantedRoles = userEntity.getRoles().stream()
            .map(RoleEntity::getName)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        // On crée et retourne un objet User en passant le login, le mot de passe et la liste des rôles.
        return new User(userEntity.getLogin(), userEntity.getPassword(), grantedRoles);
    }
}


/**
 *? Points clés :
*? UserDetailsService : C’est un contrat qui dit à Spring Security comment récupérer un utilisateur à partir d’un identifiant (ici le login).
*? UserRepository : C’est le composant qui interroge la base de données pour trouver l’utilisateur et ses rôles.
*? SimpleGrantedAuthority : Ces objets représentent les autorisations accordées à l’utilisateur. Ils seront utilisés pour contrôler l'accès aux ressources (ex. : vérification de rôle dans des annotations ou des expressions de sécurité).
  */
