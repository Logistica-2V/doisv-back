package com.logistica.doisv.dto.registro_feedback;

import com.logistica.doisv.entities.Feedback;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Schema(name = "FeedbackResumido", description = "Dados resumidos de um feedback para listagens")
public record FeedbackResumidoDTO(
        @Schema(description = "ID da solicitação associada ao feedback", example = "10")
        Long idSolicitacao,

        @Schema(description = "Primeiro nome do consumidor que realizou o feedback", example = "Maria")
        String nomeConsumidor,

        @Schema(description = "Comentário do consumidor", example = "Produto substituído rapidamente")
        String comentario,

        @Schema(description = "Nota do feedback (1 a 5)", example = "4", minimum = "1", maximum = "5")
        Integer nota,

        @Schema(description = "Data do feedback", example = "01/04/2025")
        String dataFeedback) {

    public FeedbackResumidoDTO(Feedback feedback){
        this(feedback.getSolicitacao().getId(),
                feedback.getConsumidor().getNome().split(" ")[0],
                feedback.getComentario(),
                feedback.getNota(),
                feedback.getDataFeedback()
                        .format(DateTimeFormatter
                                .ofPattern("dd/MM/yyyy", new Locale("pt", "BR"))));
    }
}
