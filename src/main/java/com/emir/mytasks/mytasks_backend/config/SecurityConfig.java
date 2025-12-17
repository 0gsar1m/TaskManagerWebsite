package com.emir.mytasks.mytasks_backend.config;

import com.emir.mytasks.mytasks_backend.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider; // Yeni eklendi
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Yeni eklendi
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.config.Customizer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. KRİTİK EKLEME: AuthenticationProvider
    // Bu metod, "Generated security password" logunu susturur ve senin DB'ni asıl kaynak yapar.
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 1) Statik dosyalar ve API Auth serbest
                        .requestMatchers("/api/auth/**", "/css/**", "/js/**", "/images/**").permitAll()

                        // 2) KRİTİK EKLEME: Admin Login sayfasına herkes erişebilsin!
                        // Bunu "/admin/**" kuralından ÖNCE yazmalısın.
                        .requestMatchers("/admin/login").permitAll()

                        // 3) Geri kalan /admin rotaları sadece ADMIN rolüne açık
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 4) Diğer her şey için giriş yapılmış olmalı
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/admin/login?logout")
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // 2. KRİTİK DEĞİŞİKLİK: Render ve Vercel için CORS
        // setAllowedOrigins yerine setAllowedOriginPatterns kullanıyoruz ki wildcard (*) çalışsın.
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:3000",      // React Local
                "http://localhost:5173",      // Vite Local
                "https://*.vercel.app",       // Vercel (Frontend)
                "https://*.onrender.com"      // Render (Backend kendine istek atarsa)
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Cache-Control",
                "Content-Type",
                "X-Requested-With"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Burası "UserDetailsService" bean'ini oluşturuyor, yukarıdaki authProvider bunu kullanacak.
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .map(user ->
                        org.springframework.security.core.userdetails.User
                                .withUsername(user.getUsername())
                                .password(user.getPasswordHash())
                                .roles(user.getRole().name())
                                .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
}