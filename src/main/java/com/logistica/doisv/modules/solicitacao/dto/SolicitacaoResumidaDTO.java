package com.logistica.doisv.modules.solicitacao.dto;

import com.logistica.doisv.modules.solicitacao.entity.Solicitacao;
import com.logistica.doisv.core.enums.Status;
import com.logistica.doisv.core.enums.StatusSolicitacao;
import com.logistica.doisv.core.enums.TipoSolicitacao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Schema(name = "SolicitacaoResumida", description = "Dados resumidos de uma solicitação")
public record SolicitacaoResumidaDTO(
        @Schema(description = "Identificador único da solicitação", example = "1")
        Long id,

        @Schema(description = "Nome do consumidor", example = "Maria da Silva")
        String consumidor,

        @Schema(description = "ID da venda associada", example = "10")
        Long idVenda,

        @Schema(description = "Descricao do produto", example = "Camiseta algodao")
        String descricaoProduto,

        @Schema(description = "Tipo da solicitação", example = "Troca")
        String tipo,

        @Schema(description = "Motivo da solicitação", example = "Produto com defeito de fabricação")
        String motivo,

        @Schema(description = "Data e hora local da solicitação", example = "01/04/2025 14:30")
        String dataSolicitacao,

        @Schema(description = "Data e hora local da última atualização", example = "02/04/2025 10:00")
        String dataAtualizacao,

        @Schema(description = "Status da solicitação", example = "Pendente")
        String statusSolicitacao,

        @Schema(description = "Status do registro (Ativo, Inativo)", example = "Ativo")
        String status
                                    ) {

    public SolicitacaoResumidaDTO(Long id, String nomeConsumidor, Long idVenda, String descricaoProduto,
                                  TipoSolicitacao tipoEnum, String motivo,
                                  LocalDateTime dataSolicitacaoTime, LocalDateTime dataAtualizacaoTime,
                                  StatusSolicitacao statusSolEnum, Status statusEnum) {
        this(id,
                nomeConsumidor,
                idVenda,
                descricaoProduto,
                tipoEnum.getDescricao(),
                motivo,
                dataSolicitacaoTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                dataAtualizacaoTime != null ?
                        dataAtualizacaoTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : null,
                statusSolEnum.getStatusSolicitacao(),
                statusEnum.getStatusItem());
    }

    public SolicitacaoResumidaDTO(Solicitacao solicitacao){
        this(solicitacao.getId(),
                solicitacao.getConsumidor().getNome(),
                solicitacao.getVenda().getId(),
                solicitacao.getItemVenda().getProduto().getDescricao(),
                solicitacao.getTipoSolicitacao().getDescricao(),
                solicitacao.getMotivo(),
                solicitacao.getDataSolicitacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                solicitacao.getDataAtualizacao() != null ? solicitacao.getDataAtualizacao()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null,
                solicitacao.getStatusSolicitacao().getStatusSolicitacao(),
                solicitacao.getStatus().getStatusItem());
    }
}
