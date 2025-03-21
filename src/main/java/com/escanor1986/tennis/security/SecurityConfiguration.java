package com.escanor1986.tennis.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Indique que cette classe contient des définitions de beans de configuration
@EnableWebSecurity // Active la sécurité web dans l'application Spring
public class SecurityConfiguration {

  // Injection du service qui charge les utilisateurs depuis la base
  private final EscanorUserDetailsService escanorUserDetailsService;

  // Constructeur pour l'injection du service
  SecurityConfiguration(EscanorUserDetailsService escanorUserDetailsService) {
    this.escanorUserDetailsService = escanorUserDetailsService;
  }

  // Déclare un bean PasswordEncoder. Ici, on utilise BCryptPasswordEncoder.
  // Ce bean est utilisé pour encoder les mots de passe lors de l'enregistrement
  // et pour vérifier
  // que le mot de passe fourni lors de l'authentification correspond au hash
  // stocké.
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // Méthode qui configure et expose un bean AuthenticationManager
  // L'AuthenticationManager est le composant central qui gère l'authentification.
  // Ici, on crée un DaoAuthenticationProvider (fournisseur d'authentification
  // basé sur des données en base).
  public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    // On définit le service de chargement des utilisateurs qui sera utilisé pour
    // récupérer les infos (EscanorUserDetailsService)
    authenticationProvider.setUserDetailsService(escanorUserDetailsService);
    // On définit l'encodeur de mot de passe, ce qui permet de comparer le mot de
    // passe saisi (après encodage) avec celui stocké en base.
    authenticationProvider.setPasswordEncoder(passwordEncoder);

    // On retourne un ProviderManager qui contient notre DaoAuthenticationProvider.
    // Le ProviderManager est responsable de traiter l'authentification en déléguant
    // à ses fournisseurs.
    return new ProviderManager(authenticationProvider);
  }

  // Déclare un bean SecurityFilterChain qui configure la sécurité HTTP.
  // Ce bean définit les règles d'accès aux URL de l'application.
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // On défini une constante pour éviter la duplication de "/players/**"
    String playersUrl = "/players/**";
    // On défini une constante pour éviter la duplication de "ROLE_ADMIN"
    String roleAdmin = "ROLE_ADMIN";
    // On définit ici que toute requête doit être authentifiée.
    // Cela signifie qu'il n'y a pas d'URL publique, l'utilisateur doit se connecter
    // pour accéder à n'importe quelle ressource.
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorization -> authorization
            .requestMatchers(HttpMethod.GET, playersUrl).hasAuthority("ROLE_USER")
            .requestMatchers(HttpMethod.POST, playersUrl).hasAuthority(roleAdmin)
            .requestMatchers(HttpMethod.PUT, playersUrl).hasAuthority(roleAdmin)
            .requestMatchers(HttpMethod.DELETE, playersUrl).hasAuthority(roleAdmin)
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/v3/api-docs/**").permitAll()
            .requestMatchers("/healthcheck/**").permitAll()
            .requestMatchers("/accounts/login").permitAll()
            .anyRequest().authenticated());
    // On construit la configuration de sécurité et on la retourne.
    return http.build();
  }
}
