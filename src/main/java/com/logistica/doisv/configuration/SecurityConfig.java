package com.logistica.doisv.configuration;

import com.logistica.doisv.services.validacao.TokenAutenticacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private TokenAutenticacao tokenAutenticacao;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/doisv/login", "/doisv/consumidores/login", "/h2-console/**", "/swagger-ui/**", "swagger-ui.html",
                                "v3/api-docs/**","swagger-resources/**").permitAll()
                        .requestMatchers("/doisv/vendas/me").hasRole("CONSUMIDOR")
                        .requestMatchers("/doisv/solicitacoes/criar").hasRole("CONSUMIDOR")
                        .requestMatchers("/doisv/produtos/**").hasRole("LOJISTA")
                        .requestMatchers("/doisv/consumidores/**").hasRole("LOJISTA")
                        .requestMatchers("/doisv/lojas/**").hasRole("LOJISTA")
                        .requestMatchers("/doisv/lojistas/**").hasRole("LOJISTA")
                        .requestMatchers("/doisv/vendas/**").hasRole("LOJISTA")
                        .anyRequest().authenticated()
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(tokenAutenticacao, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
