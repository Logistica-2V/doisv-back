package com.logistica.doisv.dto.registro_venda.resposta;

import com.logistica.doisv.entities.Venda;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Schema(name = "Venda", description = "Dados completos de uma venda")
public record VendaDTO (
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

        @Schema(description = "Data de criação da venda", example = "01/04/2025")
        String dataCriacao,

        @Schema(description = "Data de entrega da venda", example = "05/04/2025")
        String dataEntrega,

        @Schema(description = "Dados da loja associada")
        LojaVendaDTO loja,

        @Schema(description = "Dados do consumidor associado")
        ConsumidorVendaDTO consumidor,

        @Schema(description = "Status do registro (ATIVO, INATIVO)", example = "ATIVO")
        String status,

        @Schema(description = "Lista de itens da venda")
        List<ItemVendaDTO> itens) {

    public VendaDTO(Venda venda){
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
                                .ofPattern("dd/MM/yyyy", new Locale("pt", "BR"))),
                venda.getDataEntrega() != null ? venda.getDataEntrega()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "",
                new LojaVendaDTO(venda.getLoja()),
                new ConsumidorVendaDTO(venda.getConsumidor()),
                venda.getStatus().toString(),
                venda.getItensVenda().stream().map(ItemVendaDTO::new).toList());
    }
}
