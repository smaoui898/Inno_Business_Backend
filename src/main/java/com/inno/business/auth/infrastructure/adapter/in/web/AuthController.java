package com.inno.business.auth.infrastructure.adapter.in.web;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.inno.business.auth.domain.port.in.GetCurrentUserUseCase;
import com.inno.business.auth.domain.port.in.LoginUseCase;
import com.inno.business.auth.domain.port.in.RegisterUseCase;
import com.inno.business.auth.infrastructure.adapter.in.web.dto.AuthResponseDto;
import com.inno.business.auth.infrastructure.adapter.in.web.dto.LoginRequestDto;
import com.inno.business.auth.infrastructure.adapter.in.web.dto.RefreshRequestDto;
import com.inno.business.auth.infrastructure.adapter.in.web.dto.RegisterResponseDto;
import com.inno.business.auth.infrastructure.adapter.in.web.dto.UserInfoDto;
import com.inno.business.auth.infrastructure.security.JwtService;
import com.inno.business.auth.domain.model.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import jakarta.servlet.http.HttpServletResponse;


@RestController  //endpoints
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "Login et gestion des tokens") //annotation de swagger pour la documentation
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final JwtService jwtService;

    private ResponseCookie buildAccessCookie(String token) {
    return ResponseCookie.from("access_token", token)
            .httpOnly(true)              // invisible au JavaScript → anti-XSS
            .secure(false)               // false en DEV (http localhost) ; TRUE en PROD (https)
            .sameSite("Strict")          // le cookie n'est PAS envoyé par un autre site → anti-CSRF
            .path("/")                   // envoyé sur toutes les routes
            .maxAge(jwtExpiration / 1000)// maxAge est en SECONDES (jwtExpiration est en ms)
            .build();
        }
    
    private ResponseCookie buildRefreshCookie(String token) {
        return ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .secure(false)                 // false en DEV
                .sameSite("Strict")
                .path("/api/auth/refresh")     // ← envoyé UNIQUEMENT
                .maxAge(refreshExpiration / 1000)
                .build();
    }

    @Value("${jwt.expiration}")
    private long jwtExpiration;
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public AuthController(LoginUseCase loginUseCase, RegisterUseCase registerUseCase, 
                            GetCurrentUserUseCase getCurrentUserUseCase,
                            JwtService jwtService) {
        this.loginUseCase = loginUseCase;
        this.registerUseCase = registerUseCase;
        this.getCurrentUserUseCase = getCurrentUserUseCase;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion", description = "Retourne un JWT (mobile) ou pose un cookie (web)")
    public ResponseEntity<?> login(
        @RequestBody LoginRequestDto request, 
        @RequestHeader(value = "X-Client-Type", defaultValue = "mobile") String clientType,
        HttpServletResponse response
    ) {
        LoginUseCase.LoginResult result = loginUseCase.execute(
                new LoginUseCase.LoginCommand(request.email(), request.password()));
        if (clientType.equals("web")) {
        // WEB : le token part en cookie httpOnly, le body ne contient PAS le token
                ResponseCookie cookie = buildAccessCookie(result.token());
                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
                response.addHeader(HttpHeaders.SET_COOKIE, buildRefreshCookie(result.refreshToken()).toString());
                return ResponseEntity.ok(new UserInfoDto(result.email(), result.role(), result.prenom(), result.nom()));
        }
        // MOBILE : comportement actuel, token dans le JSON
        return ResponseEntity.ok(new AuthResponseDto(
            result.token(), result.refreshToken(), result.email(), result.role(), result.prenom(), result.nom()));
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

    @GetMapping("/me")
    @Operation(summary = "Profil courant", description = "Retourne l'utilisateur authentifié")
        public ResponseEntity<UserInfoDto> me(@AuthenticationPrincipal UserDetails principal) {
        // si on arrive ici, le filtre a validé le cookie/token → principal est renseigné
            User user = getCurrentUserUseCase.execute(principal.getUsername());  // getUsername() = email
            return ResponseEntity.ok(new UserInfoDto(
                user.getEmailValue(), user.getRole(), user.getPrenom(), user.getNom()));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renouveler l'access token", description = "Via le refresh token (cookie web ou body mobile)")
    public ResponseEntity<?> refresh(
            @RequestHeader(value = "X-Client-Type", defaultValue = "mobile") String clientType,
            @CookieValue(value = "refresh_token", required = false) String cookieRefresh,
            @RequestBody(required = false) RefreshRequestDto body,
            HttpServletResponse response) {

        String refreshToken = clientType.equals("web")
                ? cookieRefresh
                : (body != null ? body.refreshToken() : null);

        if (refreshToken == null || !jwtService.isRefreshTokenValid(refreshToken)) {
            return ResponseEntity.status(401).body(Map.of("error", "Refresh token invalide ou expiré"));
        }

        String email = jwtService.extractEmail(refreshToken);
        String newAccess = jwtService.generateAccessToken(email);

        if (clientType.equals("web")) {
            response.addHeader(HttpHeaders.SET_COOKIE, buildAccessCookie(newAccess).toString());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok(Map.of("token", newAccess));
    }

}