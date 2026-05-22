package com.logistica.doisv.modules.metrica.controller;

import com.logistica.doisv.modules.metrica.docs.MetricaApi;
import com.logistica.doisv.modules.autenticacao.dto.AcessoDTO;
import com.logistica.doisv.modules.metrica.dto.MetricasPrivadasDTO;
import com.logistica.doisv.modules.metrica.dto.MetricasPublicasLojaDTO;
import com.logistica.doisv.modules.metrica.service.MetricaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("doisv/metricas")
public class MetricaController implements MetricaApi {

    @Autowired
    private MetricaService metricaService;

    @GetMapping("/privadas")
    public ResponseEntity<MetricasPrivadasDTO> buscarMetricasPrivadas(@RequestParam(defaultValue = "365") Integer periodo,
                                                                      @AuthenticationPrincipal AcessoDTO usuarioLogado){

        return ResponseEntity.ok(metricaService.metricasPrivadasPorLojaEPeriodo(usuarioLogado.getIdLoja(), periodo));
    }

    @GetMapping("/publicas")
    public ResponseEntity<Page<MetricasPublicasLojaDTO>> buscarMetricasPublicas(Pageable pageable, @RequestParam(defaultValue = "180") Integer periodo){
        return ResponseEntity.ok(metricaService.metricasPublicasTodasLojas(pageable, periodo));
    }
}
