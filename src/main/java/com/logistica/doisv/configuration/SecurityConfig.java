package com.logistica.doisv.configuration;

import com.logistica.doisv.services.validacao.TokenAutenticacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                .headers(headers ->
                        headers.frameOptions(frameOptions -> frameOptions.disable()))
                .authorizeHttpRequests(auth -> auth

                        // ===================== ROTAS PÚBLICAS =====================
                        .requestMatchers(
                                "/doisv/login",
                                "/doisv/login/consumidores",
                                "/doisv/login/lojista/recuperar-senha",
                                "/doisv/login/lojista/validar-recuperacao",
                                "/doisv/login/lojista/atualizar-senha",
                                "/doisv/metricas/publicas",
                                "/doisv/feedbacks/lojas/{id}",
                                "/h2-console/**",
                                "/swagger-ui/**",
                                "swagger-ui.html",
                                "v3/api-docs/**",
                                "swagger-resources/**"
                        ).permitAll()

                        // ===================== ROTAS DELETE (ADMIN) =====================
                        .requestMatchers(HttpMethod.DELETE, "doisv/lojas/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/doisv/**").hasRole("ADMIN")

                        // ===================== ROTAS CONSUMIDOR =====================
                        .requestMatchers("/doisv/vendas/me").hasRole("CONSUMIDOR")
                        .requestMatchers(HttpMethod.PUT, "/doisv/solicitacoes/{id}").hasRole("CONSUMIDOR")
                        .requestMatchers("/doisv/solicitacoes/criar").hasRole("CONSUMIDOR")
                        .requestMatchers("/doisv/solicitacoes/cancelar/{id}").hasRole("CONSUMIDOR")
                        .requestMatchers(HttpMethod.POST, "/doisv/feedbacks").hasRole("CONSUMIDOR")

                        // ===================== ROTAS LOJISTA =====================
                        .requestMatchers(HttpMethod.GET, "/doisv/lojistas/profile").hasRole("LOJISTA")
                        .requestMatchers(HttpMethod.GET, "/doisv/solicitacoes").hasRole("LOJISTA")
                        .requestMatchers("/doisv/solicitacoes/aprovar/{id}").hasRole("LOJISTA")
                        .requestMatchers("/doisv/solicitacoes/reprovar/{id}").hasRole("LOJISTA")
                        .requestMatchers("/doisv/solicitacoes/atualizar/{id}").hasRole("LOJISTA")
                        .requestMatchers("/doisv/metricas/privadas").hasRole("LOJISTA")
                        .requestMatchers("/doisv/metricas/solicitacoes/por-status").hasRole("LOJISTA")
                        .requestMatchers("/doisv/produtos/**").hasRole("LOJISTA")
                        .requestMatchers("/doisv/consumidores/**").hasRole("LOJISTA")
                        .requestMatchers("/doisv/vendas/**").hasRole("LOJISTA")
                        .requestMatchers("/doisv/feedbacks/{id}").hasRole("LOJISTA")
                        .requestMatchers("/doisv/relatorios/**").hasRole("LOJISTA")

                        // ===================== ROTAS LOJISTA OU CONSUMIDOR =====================
                        .requestMatchers(HttpMethod.GET, "/doisv/feedbacks/solicitacoes/{id}")
                                        .hasAnyRole("LOJISTA", "CONSUMIDOR")

                        // ===================== ROTAS MASTER =====================
                        .requestMatchers("/doisv/lojas/**").hasRole("MASTER")

                        // ===================== ROTAS ADMIN =====================
                        .requestMatchers(HttpMethod.POST, "doisv/lojistas").hasRole("ADMIN")
                        .requestMatchers("/doisv/lojistas/**").hasRole("ADMIN")

                        // ===================== DEMAIS ROTAS =====================
                        .anyRequest().authenticated()

                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(tokenAutenticacao, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}