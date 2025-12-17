package com.emir.mytasks.mytasks_backend.controller.api;

import com.emir.mytasks.mytasks_backend.dto.AuthResponse;
import com.emir.mytasks.mytasks_backend.dto.LoginRequest;
import com.emir.mytasks.mytasks_backend.dto.RegisterRequest;
import com.emir.mytasks.mytasks_backend.entity.User;
import com.emir.mytasks.mytasks_backend.repository.UserRepository;
import com.emir.mytasks.mytasks_backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.AuthenticationException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          SecurityContextRepository securityContextRepository) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.securityContextRepository = securityContextRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = authService.registerUser(request);

            AuthResponse response = new AuthResponse(
                    "Registration successful",
                    user.getUsername(),
                    user.getRole().name()
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) {
            // username/email çakışması gibi durumlarda buraya düşecek
            return ResponseEntity.badRequest().body(
                    java.util.Map.of(
                            "error", ex.getMessage()
                    )
            );
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request,
                                   HttpServletRequest httpRequest,
                                   HttpServletResponse httpResponse) {

        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    );

            // 1) Kullanıcıyı doğrula
            Authentication authentication = authenticationManager.authenticate(authToken);

            // 2) SecurityContext oluştur ve Authentication'ı içine koy
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);

            // 3) Context'i session'a kaydet (kritik satır)
            securityContextRepository.saveContext(securityContext, httpRequest, httpResponse);

            // 4) Role bilgisini toparla
            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("ROLE_USER");

            if (role.startsWith("ROLE_")) {
                role = role.substring("ROLE_".length());
            }

            AuthResponse response = new AuthResponse(
                    "Login successful",
                    request.getUsername(),
                    role
            );

            return ResponseEntity.ok(response);

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401)
                    .body(java.util.Map.of(
                            "error", "Invalid username or password"
                    ));
        }
    }


}
