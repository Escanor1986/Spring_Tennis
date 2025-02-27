package com.escanor1986.tennis.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration                   // Indique que cette classe contient des définitions de beans de configuration
@EnableWebSecurity               // Active la sécurité web dans l'application Spring
public class SecurityConfiguration {

  // Injection du service qui charge les utilisateurs depuis la base
  private final EscanorUserDetailsService escanorUserDetailsService;

  // Constructeur pour l'injection du service
  SecurityConfiguration(EscanorUserDetailsService escanorUserDetailsService) {
      this.escanorUserDetailsService = escanorUserDetailsService;
  }

  // Déclare un bean PasswordEncoder. Ici, on utilise BCryptPasswordEncoder.
  // Ce bean est utilisé pour encoder les mots de passe lors de l'enregistrement et pour vérifier
  // que le mot de passe fourni lors de l'authentification correspond au hash stocké.
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // Méthode qui configure et expose un bean AuthenticationManager
  // L'AuthenticationManager est le composant central qui gère l'authentification.
  // Ici, on crée un DaoAuthenticationProvider (fournisseur d'authentification basé sur des données en base).
  public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    // On définit le service de chargement des utilisateurs qui sera utilisé pour récupérer les infos (EscanorUserDetailsService)
    authenticationProvider.setUserDetailsService(escanorUserDetailsService);
    // On définit l'encodeur de mot de passe, ce qui permet de comparer le mot de passe saisi (après encodage)
    // avec celui stocké en base.
    authenticationProvider.setPasswordEncoder(passwordEncoder);

    // On retourne un ProviderManager qui contient notre DaoAuthenticationProvider.
    // Le ProviderManager est responsable de traiter l'authentification en déléguant à ses fournisseurs.
    return new ProviderManager(authenticationProvider);
  }

  // Déclare un bean SecurityFilterChain qui configure la sécurité HTTP.
  // Ce bean définit les règles d'accès aux URL de l'application.
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // On définit ici que toute requête doit être authentifiée.
    // Cela signifie qu'il n'y a pas d'URL publique, l'utilisateur doit se connecter pour accéder à n'importe quelle ressource.
    http.authorizeHttpRequests(authorization -> authorization.anyRequest().authenticated());
    // On construit la configuration de sécurité et on la retourne.
    return http.build();
  }
}

/**
 *? Points clés :
*? @Configuration et @EnableWebSecurity : Ces annotations indiquent à Spring que cette classe contient la configuration de sécurité pour l’application.
*? PasswordEncoder :
*? Le BCryptPasswordEncoder est un algorithme de hachage sécurisé pour stocker les mots de passe.
*? Lorsqu’un utilisateur se connecte, le mot de passe saisi est encodé et comparé avec le hash stocké.
*? AuthenticationManager :
*? Il est chargé de vérifier les informations d’identification (login/mot de passe).
*? Le DaoAuthenticationProvider est configuré avec notre service de chargement des utilisateurs et l’encodeur.
*? Si les informations sont correctes, l’utilisateur est authentifié et ses rôles (autorités) sont attachés à son contexte de sécurité.
*? SecurityFilterChain :
*? Cette configuration déclare qu’aucune requête n’est accessible sans authentification.
*? Vous pouvez étendre cette configuration pour autoriser certains endpoints en public ou définir des règles plus fines (ex. : accès selon le rôle).
  */
