package com.logistica.doisv.dto;

import java.math.BigDecimal;
import java.util.Map;

public record MetricasPublicasLojaDTO(Long idLoja,
                                      String nomeLoja,
                                      String logo,
                                      String segmento,
                                      Integer totalAvaliacoes,
                                      Map<String, BigDecimal> notaMedia) {
}
