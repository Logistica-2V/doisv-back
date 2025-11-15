package com.logistica.doisv.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record HistoricoSolicitacaoDTO(Long id, String statusAnterior, @NotBlank String statusNovo, LocalDateTime dataAtualizacao, @NotBlank String observacao) {
}
