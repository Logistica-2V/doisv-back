package com.logistica.doisv.controllers.docs;

import com.fasterxml.jackson.databind.JsonNode;
import com.logistica.doisv.dto.ConsumidorLoginDTO;
import com.logistica.doisv.dto.LoginDTO;
import com.logistica.doisv.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Autenticação", description = "Endpoints de login e recuperação de senha")
@RequestMapping("/doisv/login")
public interface LoginApi {

    @Operation(summary = "Login do lojista",
            description = "Autentica um lojista com e-mail e senha, retornando um token JWT em caso de sucesso.",
            operationId = "loginLojista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Email ou senha inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "Email ou senha inválidos"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping
    ResponseEntity<?> loginLojista(
            @Parameter(description = "Credenciais do lojista", required = true)
            @Valid @RequestBody LoginDTO dto);

    @Operation(summary = "Login do consumidor",
            description = "Autentica um consumidor com serial e senha, retornando um token JWT em caso de sucesso.",
            operationId = "loginConsumidor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Serial ou senha inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "Serial ou senha inválidos"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping(value = "/consumidores")
    ResponseEntity<?> loginConsumidor(
            @Parameter(description = "Credenciais do consumidor", required = true)
            @Valid @RequestBody ConsumidorLoginDTO dto);

    @Operation(summary = "Recuperar senha do lojista",
            description = "Envia um código de recuperação de senha para o e-mail informado.",
            operationId = "recuperarSenha")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "E-mail do lojista para recuperação",
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "object"),
                    examples = @ExampleObject(value = "{\"email\": \"lojista@email.com\"}")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Código de recuperação enviado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "Código de recuperação enviado para o email lojista@email.com"))),
            @ApiResponse(responseCode = "400", description = "E-mail não informado", content = @Content),
            @ApiResponse(responseCode = "404", description = "E-mail não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping(value = "/lojista/recuperar-senha")
    ResponseEntity<String> recuperarSenha(
            @RequestBody JsonNode body) throws MessagingException;

    @Operation(summary = "Validar código de recuperação",
            description = "Valida o código de recuperação de senha recebido por e-mail.",
            operationId = "validarCodigoRecuperacao")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "E-mail e código de recuperação",
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "object"),
                    examples = @ExampleObject(value = "{\"email\": \"lojista@email.com\", \"codigoRecuperacao\": \"123456\"}")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Código validado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou código expirado", content = @Content),
            @ApiResponse(responseCode = "404", description = "E-mail não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping(value = "/lojista/validar-recuperacao")
    ResponseEntity<Void> validarCodigoRecuperacao(
            @RequestBody JsonNode body);

    @Operation(summary = "Atualizar senha do lojista",
            description = "Atualiza a senha do lojista após a validação do código de recuperação.",
            operationId = "atualizarSenha")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "E-mail, código de recuperação e nova senha",
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "object"),
                    examples = @ExampleObject(value = "{\"email\": \"lojista@email.com\", \"codigoRecuperacao\": \"123456\", \"novaSenha\": \"NovaSenha@123\"}")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "Senha atualizada com sucesso."))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou código expirado", content = @Content),
            @ApiResponse(responseCode = "404", description = "E-mail não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping(value = "/lojista/atualizar-senha")
    ResponseEntity<String> atualizarSenha(
            @RequestBody JsonNode body);
}
