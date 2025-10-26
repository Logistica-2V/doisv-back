package com.logistica.doisv.dto;

import jakarta.validation.constraints.NotBlank;

public record ConsumidorLoginDTO(@NotBlank String serial,@NotBlank String senha) {
}
