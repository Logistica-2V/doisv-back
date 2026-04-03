package com.logistica.doisv.dto.registro_venda.requisicao;

import com.logistica.doisv.entities.Solicitacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

@Schema(name = "RegistroVenda", description = "Dados para criação ou atualização de uma venda")
public record RegistroVendaDTO(
        @Schema(description = "Valor de desconto aplicado à venda", example = "15.50")
        @DecimalMin(value = "0.0", message = "O desconto deve ser maior ou igual a 0,00.")
        @Digits(integer = 6, fraction = 2, message = "O desconto deve ter no máximo 6 dígitos inteiros e 2 casas decimais.")
        BigDecimal desconto,

        @Schema(description = "ID do consumidor associado à venda", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O ID do consumidor é obrigatório.") Long idConsumidor,

        @Schema(description = "Prazo de troca em dias", example = "30")
        @PositiveOrZero(message = "O prazo de troca não pode ser negativo.")
        Integer prazoTroca,

        @Schema(description = "Prazo de devolução em dias", example = "7")
        @PositiveOrZero(message = "O prazo de devolução não pode ser negativo.")
        Integer prazoDevolucao,

        @Schema(description = "Forma de pagamento utilizada", example = "Cartão de Crédito")
        String formaPagamento,

        @Schema(description = "Status do pedido", example = "Entregue", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O status do pedido é obrigatório.") String statusPedido,

        @Schema(description = "Lista de itens da venda", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "A venda deve conter ao menos um item.") List<ItemDTO> itensVenda) {

    public RegistroVendaDTO(Solicitacao solicitacao, List<ItemDTO> novosProdutos){
        this(BigDecimal.valueOf(0),
                solicitacao.getConsumidor().getIdConsumidor(),
                solicitacao.getVenda().getPrazoTroca(),
                solicitacao.getVenda().getPrazoDevolucao(),
                String.format("Venda gerada a partir da %s ID: %d", solicitacao.getTipoSolicitacao().getDescricao(), solicitacao.getId()),
                "Entregue",
                novosProdutos);
    }
}
