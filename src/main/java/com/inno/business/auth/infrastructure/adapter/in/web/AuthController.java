package com.inno.business.auth.infrastructure.adapter.in.web;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.inno.business.auth.domain.port.in.LoginUseCase;
import com.inno.business.auth.domain.port.in.RegisterUseCase;
import com.inno.business.auth.infrastructure.adapter.in.web.dto.AuthResponseDto;
import com.inno.business.auth.infrastructure.adapter.in.web.dto.LoginRequestDto;
import com.inno.business.auth.infrastructure.adapter.in.web.dto.RegisterResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController  //endpoints
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Authentification", description = "Login et gestion des tokens") //annotation de swagger pour la documentation
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;

    public AuthController(LoginUseCase loginUseCase, RegisterUseCase registerUseCase) {
        this.loginUseCase = loginUseCase;
        this.registerUseCase = registerUseCase;
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion", description = "Retourne un JWT token si les identifiants sont valides")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        LoginUseCase.LoginResult result = loginUseCase.execute(
                new LoginUseCase.LoginCommand(request.email(), request.password())
        );
        return ResponseEntity.ok(new AuthResponseDto(result.token(), result.email(), result.role(), result.prenom(), result.nom()));
    }
    
    @PostMapping(value = "/register", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Creation de compte", description = "Cree un compte utilisateur")
    public ResponseEntity<RegisterResponseDto> register(

            // Étape 1
            @RequestParam String prenom,
            @RequestParam String nom,
            @RequestParam String telephone,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,

            // Étape 2  
            @RequestParam String typeSociete,      // BUSINESS ou INSTANT
            @RequestParam String nomGroupe,
            @RequestParam String raisonSociale,
            @RequestParam(required = false) String identifiantUnique,
            @RequestParam(required = false) String adresse,
            @RequestParam(required = false) String ville,
            @RequestParam(required = false) String emailSociete,
            @RequestParam(required = false) String telephoneSociete,

            // Étape 3
            @RequestParam MultipartFile rneDocument,
            @RequestParam MultipartFile patenteDocument,
            @RequestParam MultipartFile cinGerantDocument

    ) throws IOException {

        RegisterUseCase.RegisterResult result = registerUseCase.execute(
                new RegisterUseCase.RegisterCommand(
                        // Étape 1
                        prenom, nom, telephone, email, password, confirmPassword,
                        // Étape 2
                        typeSociete, nomGroupe, raisonSociale, identifiantUnique,
                        adresse, ville, emailSociete, telephoneSociete,
                        // Étape 3
                        rneDocument.getBytes(),      rneDocument.getOriginalFilename(),
                        patenteDocument.getBytes(),  patenteDocument.getOriginalFilename(),
                        cinGerantDocument.getBytes(),cinGerantDocument.getOriginalFilename()
                )
        );

        return ResponseEntity.status(201)
                .body(new RegisterResponseDto(result.message(), result.email()));
    }

}