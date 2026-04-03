package com.logistica.doisv.dto.registro_solicitacao;

import com.logistica.doisv.dto.ConsumidorDTO;
import com.logistica.doisv.dto.registro_feedback.FeedbackDTO;
import com.logistica.doisv.entities.AnexoSolicitacao;
import com.logistica.doisv.entities.Solicitacao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Schema(name = "SolicitacaoDetalhada", description = "Dados detalhados de uma solicitação, incluindo venda, consumidor, produto, anexos, histórico e feedbacks")
public record SolicitacaoDetalhadaDTO(
        @Schema(description = "Identificador único da solicitação", example = "1")
        Long id,

        @Schema(description = "Tipo da solicitação", example = "Troca")
        String tipo,

        @Schema(description = "Quantidade solicitada", example = "2.0")
        Double quantidade,

        @Schema(description = "Motivo da solicitação", example = "Produto com defeito de fabricação")
        String motivo,

        @Schema(description = "Data da solicitação", example = "01/04/2025 14:30")
        String dataSolicitacao,

        @Schema(description = "Data da última atualização", example = "02/04/2025 10:00")
        String dataAtualizacao,

        @Schema(description = "Status da solicitação", example = "Pendente")
        String statusSolicitacao,

        @Schema(description = "Status do registro (Ativo, Inativo)", example = "Ativo")
        String status,

        @Schema(description = "Dados da venda associada")
        VendaSolicitacaoDTO venda,

        @Schema(description = "Dados do consumidor")
        ConsumidorDTO consumidor,

        @Schema(description = "Dados do produto solicitado")
        ProdutoSolicitacaoDTO produto,

        @Schema(description = "Lista de URLs dos anexos")
        List<String> anexos,

        @Schema(description = "Histórico de atualizações da solicitação")
        List<HistoricoSolicitacaoDTO> historico,

        @Schema(description = "Lista de feedbacks da solicitação")
        List<FeedbackDTO> feedbacks) {

    public SolicitacaoDetalhadaDTO(Solicitacao solicitacao) {
        this(solicitacao.getId(),
                solicitacao.getTipoSolicitacao().getDescricao(),
                solicitacao.getQuantidade(),
                solicitacao.getMotivo(),
                solicitacao.getDataSolicitacao().atZone(ZoneId.of("America/Sao_Paulo"))
                        .format(DateTimeFormatter
                                .ofPattern("dd/MM/yyyy HH:mm", new Locale("pt", "BR"))),
                solicitacao.getDataAtualizacao() != null ? solicitacao.getDataAtualizacao()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : null,
                solicitacao.getStatusSolicitacao().getStatusSolicitacao(),
                solicitacao.getStatus().getStatusItem(),
                new VendaSolicitacaoDTO(solicitacao.getVenda()),
                new ConsumidorDTO(solicitacao.getConsumidor()),
                new ProdutoSolicitacaoDTO(solicitacao.getItemVenda()),
                solicitacao.getAnexos().stream().map(AnexoSolicitacao::getUrlImagem).collect(Collectors.toList()),
                solicitacao.getHistoricos().stream().map(HistoricoSolicitacaoDTO::new).toList(),
                solicitacao.getFeedbacks().stream().map(FeedbackDTO::new).toList());
    }
}
