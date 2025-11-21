package com.logistica.doisv.dto.registro_venda.requisicao;

import com.logistica.doisv.entities.Solicitacao;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record RegistroVendaDTO(@DecimalMin(value = "0.0") @Digits(integer = 6, fraction = 2) BigDecimal desconto, @NotNull Long idConsumidor,
                               @PositiveOrZero Integer prazoTroca, @PositiveOrZero Integer prazoDevolucao,
                               String formaPagamento, @NotBlank String statusPedido, @NotEmpty List<ItemDTO> itensVenda) {

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
