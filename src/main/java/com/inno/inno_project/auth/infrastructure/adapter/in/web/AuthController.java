package com.inno.inno_project.auth.infrastructure.adapter.in.web;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.inno.inno_project.auth.domain.port.in.LoginUseCase;
import com.inno.inno_project.auth.domain.port.in.RegisterUseCase;
import com.inno.inno_project.auth.infrastructure.adapter.in.web.dto.AuthResponseDto;
import com.inno.inno_project.auth.infrastructure.adapter.in.web.dto.LoginRequestDto;
import com.inno.inno_project.auth.infrastructure.adapter.in.web.dto.RegisterResponseDto;

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
    @Operation(summary = "Connexion utilisateur", description = "Retourne un JWT token si les identifiants sont valides")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        LoginUseCase.LoginResult result = loginUseCase.execute(
                new LoginUseCase.LoginCommand(request.email(), request.password())
        );
        return ResponseEntity.ok(new AuthResponseDto(result.token(), result.email(), result.role(), result.prenom(), result.nom()));
    }
    
    @PostMapping(value = "/register", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Creation de compte", description = "Cree un compte utilisateur")
    public ResponseEntity<RegisterResponseDto> register(
        @RequestParam String prenom,
        @RequestParam String nom,
        @RequestParam String telephone,
        @RequestParam String email,
        @RequestParam String password,
        @RequestParam String confirmPassword,
        @RequestParam String nomGroupe,
        @RequestParam String raisonSociale,
        @RequestParam String identifiantUnique,
        @RequestParam String adresse,
        @RequestParam String ville,
        @RequestParam String emailSociete,
        @RequestParam String telephoneSociete,
        @org.springframework.web.bind.annotation.RequestPart("rneFileDocument") MultipartFile rneFileDocument,
        @org.springframework.web.bind.annotation.RequestPart("patenteFileDocument") MultipartFile patenteFileDocument,
        @org.springframework.web.bind.annotation.RequestPart("cinGerantFileDocument") MultipartFile cinGerantFileDocument
    )
    {
        try {
            RegisterUseCase.RegisterResult result = registerUseCase.execute(
                new RegisterUseCase.RegisterCommand(
                    prenom,
                    nom,
                    telephone,
                    email,
                    password,
                    confirmPassword,
                    nomGroupe,
                    raisonSociale,
                    identifiantUnique,
                    adresse,        
                    ville,
                    emailSociete,
                    telephoneSociete,
                    rneFileDocument.getBytes(),
                    rneFileDocument.getOriginalFilename(),
                    patenteFileDocument.getBytes(),
                    patenteFileDocument.getOriginalFilename(),
                    cinGerantFileDocument.getBytes(),
                    cinGerantFileDocument.getOriginalFilename()
                )
            );
            return ResponseEntity.ok(new RegisterResponseDto(result.message(), result.email()));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
}
