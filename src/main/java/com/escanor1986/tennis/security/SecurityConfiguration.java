package com.escanor1986.tennis.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de sécurité de l'application.
 * Cette classe définit l'ensemble des règles d'authentification et d'autorisation,
 * ainsi que les configurations de sécurité HTTP pour l'application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /**
     * Service personnalisé qui charge les détails des utilisateurs pour l'authentification.
     * Cette implémentation permet de récupérer les informations utilisateur depuis notre base de données.
     */
    private final EscanorUserDetailsService escanorUserDetailsService;

    /**
     * Constructeur pour l'injection du service UserDetailsService.
     * 
     * @param escanorUserDetailsService Le service qui charge les informations des utilisateurs
     */
    public SecurityConfiguration(EscanorUserDetailsService escanorUserDetailsService) {
        this.escanorUserDetailsService = escanorUserDetailsService;
    }

    /**
     * Configure l'encodeur de mot de passe utilisé par l'application.
     * BCrypt est un algorithme de hachage sécurisé avec salage intégré.
     * 
     * @return Un encodeur de mot de passe BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure le gestionnaire d'authentification qui vérifie les identifiants utilisateur.
     * 
     * @param passwordEncoder L'encodeur de mot de passe utilisé pour vérifier les identifiants
     * @return Un gestionnaire d'authentification configuré
     */
    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        // Configuration du service de chargement des utilisateurs
        authenticationProvider.setUserDetailsService(escanorUserDetailsService);
        // Configuration de l'encodeur pour la vérification des mots de passe
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        // Création du gestionnaire d'authentification avec notre fournisseur
        return new ProviderManager(authenticationProvider);
    }

    /**
     * Configure la chaîne de filtres de sécurité qui définit les règles d'accès aux ressources.
     * Cette méthode établit quelles URLs sont accessibles selon les rôles et les permissions.
     * 
     * @param http L'objet de configuration de sécurité HTTP
     * @return La chaîne de filtres de sécurité configurée
     * @throws Exception Si une erreur survient pendant la configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Constantes pour éviter la duplication et faciliter la maintenance
        final String playersUrl = "/players/**";
        final String roleAdmin = "ROLE_ADMIN";
        final String roleUser = "ROLE_USER";
        
        http
            // Désactivation de la protection CSRF pour simplifier les appels API
            .csrf(csrf -> csrf.disable())
            
            // Configuration des en-têtes de sécurité HTTP
            .headers(headers -> headers
                // Content Security Policy : Définit les sources autorisées pour le contenu
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives("default-src 'self'; style-src 'self' 'unsafe-inline';"))
                // X-Frame-Options : Empêche le site d'être affiché dans un iframe (protection contre le clickjacking)
                .frameOptions(frameOptions -> frameOptions.deny())
                // Cache-Control : Désactive la mise en cache pour les ressources sensibles
                .cacheControl(cacheControl -> cacheControl.disable())
            )
            
            // Configuration des règles d'autorisation pour les requêtes HTTP
            .authorizeHttpRequests(authorization -> authorization
                // Règles pour l'API des joueurs selon la méthode HTTP et le rôle
                .requestMatchers(HttpMethod.GET, playersUrl).hasAuthority(roleUser)
                .requestMatchers(HttpMethod.POST, playersUrl).hasAuthority(roleAdmin)
                .requestMatchers(HttpMethod.PUT, playersUrl).hasAuthority(roleAdmin)
                .requestMatchers(HttpMethod.DELETE, playersUrl).hasAuthority(roleAdmin)
                .requestMatchers("/actuator/**").hasAuthority(roleAdmin)
                
                // URLs publiques ne nécessitant pas d'authentification
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/healthcheck/**").permitAll()
                .requestMatchers("/accounts/login").permitAll()
                
                // Toutes les autres requêtes nécessitent une authentification
                .anyRequest().authenticated()
            );
            
        // Construction et retour de la chaîne de filtres configurée
        return http.build();
    }
}
