package com.logistica.doisv.dto.registro_solicitacao;

import com.logistica.doisv.entities.Venda;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Schema(name = "VendaSolicitacao", description = "Dados resumidos da venda associada a uma solicitação")
public record VendaSolicitacaoDTO(
        @Schema(description = "Identificador único da venda", example = "1")
        Long idVenda,

        @Schema(description = "Código serial da venda", example = "VND-20250401-001")
        String serial,

        @Schema(description = "Preço total da venda", example = "299.90")
        BigDecimal precoTotal,

        @Schema(description = "Status do pedido", example = "Entregue")
        String statusPedido,

        @Schema(description = "Valor de desconto aplicado", example = "15.50")
        BigDecimal desconto,

        @Schema(description = "Forma de pagamento utilizada", example = "Cartão de Crédito")
        String formaPagamento,

        @Schema(description = "Prazo de troca em dias", example = "30")
        Integer prazoTroca,

        @Schema(description = "Prazo de devolução em dias", example = "7")
        Integer prazoDevolucao,

        @Schema(description = "Data de criação da venda", example = "01/04/2025 14:30")
        String dataCriacao,

        @Schema(description = "Data de entrega da venda", example = "05/04/2025")
        String dataEntrega) {

    public VendaSolicitacaoDTO(Venda venda){
        this(venda.getId(),
                venda.getSerialVenda(),
                venda.getPrecoTotal(),
                venda.getStatusPedido().getStatusPedido(),
                venda.getDesconto(),
                venda.getFormaPagamento(),
                venda.getPrazoTroca(),
                venda.getPrazoDevolucao(),
                venda.getDataCriacao().atZone(ZoneId.of("America/Sao_Paulo"))
                        .format(DateTimeFormatter
                                .ofPattern("dd/MM/yyyy HH:mm", new Locale("pt", "BR"))),
                venda.getDataEntrega().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
}
