package com.logistica.doisv.controllers;

import com.logistica.doisv.dto.AcessoDTO;
import com.logistica.doisv.services.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("doisv/relatorios")
public class RelatorioController {

    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping("/{relatorio}/excel")
    public ResponseEntity<byte[]> exportarExcel(@PathVariable String relatorio,
                                                @AuthenticationPrincipal AcessoDTO usuarioLogado) {

        byte[] arquivo = excelExportService.exportar(relatorio, usuarioLogado.getIdLoja());

        String nomeArquivo = "relatorio_" + relatorio + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(arquivo.length)
                .body(arquivo);
    }
}
