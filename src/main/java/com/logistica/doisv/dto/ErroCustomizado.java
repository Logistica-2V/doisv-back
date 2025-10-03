package com.logistica.doisv.dto;

import java.time.Instant;

public record ErroCustomizado(Instant instante, Integer codigo, String mensagem, String url) {
}
