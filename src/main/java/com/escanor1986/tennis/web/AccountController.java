package com.escanor1986.tennis.web;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import com.escanor1986.tennis.model.UserCredentials;

@Tag(name = "Accounts API")
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    private final SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();

    AccountController(AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @Operation(summary = "Authenticates user", description = "Authenticates user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is logged in."),
            @ApiResponse(responseCode = "403", description = "User credentials are not valid."),
            @ApiResponse(responseCode = "400", description = "Login or password is not provided.")
    })
    @PostMapping("/login")
    // @RequestBody : Indique que les données de la requête HTTP doivent être désérialisées en objet UserCredentials
    // @Valid : Indique à Spring de valider les données de la requête en utilisant les annotations de validation de l'objet UserCredentials
    public void login(@RequestBody @Valid UserCredentials credentials, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(credentials.login(), credentials.password());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        securityContextRepository.saveContext(securityContext, request, response);
    }

    @Operation(summary = "Logs off authenticated user", description = "Logs off authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is logged out."),
            @ApiResponse(responseCode = "403", description = "No user is logged in.")
    })
    @GetMapping("/logout")
    public void logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        securityContextLogoutHandler.logout(request, response, authentication);
    }

}
