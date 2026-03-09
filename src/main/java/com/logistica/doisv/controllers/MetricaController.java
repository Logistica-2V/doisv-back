    package com.logistica.doisv.controllers;

    import com.logistica.doisv.dto.AcessoDTO;
    import com.logistica.doisv.dto.MetricasPrivadasDTO;
    import com.logistica.doisv.dto.MetricasPublicasLojaDTO;
    import com.logistica.doisv.services.MetricaService;
    import com.logistica.doisv.services.validacao.TokenService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.Map;

    @RestController
    @RequestMapping("doisv/metricas")
    public class MetricaController {

        @Autowired
        private MetricaService metricaService;

        @Autowired
        private TokenService tokenService;

        @GetMapping("/privadas")
        public ResponseEntity<MetricasPrivadasDTO> buscarMetricasPrivadas(@RequestParam(defaultValue = "365") Integer periodo,
                                                                          @RequestHeader String Authorization){
            AcessoDTO acesso = tokenService.validarToken(Authorization);
            return ResponseEntity.ok(metricaService.metricasPrivadasPorLojaEPeriodo(acesso.getIdLoja(), periodo));
        }

        @GetMapping("/publicas")
        public ResponseEntity<Page<MetricasPublicasLojaDTO>> buscarMetricasPublicas(Pageable pageable, @RequestParam(defaultValue = "180") Integer periodo){
            return ResponseEntity.ok(metricaService.obterMetricasPublicasTodasLojas(pageable, periodo));
        }

        @GetMapping("/solicitacoes/por-status")
        public ResponseEntity<Map<String, Integer>> buscarQuantidadeSolicitacoesPorStatus(@RequestParam(value = "periodo", defaultValue = "365") Integer periodo,
                                                                                          @RequestHeader String Authorization){
            AcessoDTO acessoDTO = tokenService.validarToken(Authorization);
            return ResponseEntity.ok(metricaService.obterQuantidadeSolicitacoes(acessoDTO.getIdLoja(), periodo));
        }
    }
