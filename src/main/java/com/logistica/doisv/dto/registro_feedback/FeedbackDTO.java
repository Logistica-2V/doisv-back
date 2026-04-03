package com.logistica.doisv.dto.registro_feedback;

import com.logistica.doisv.entities.Feedback;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Schema(name = "Feedback", description = "Dados de um feedback sobre uma solicitação")
public record FeedbackDTO(
        @Schema(description = "Identificador único do feedback", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long idFeedback,

        @Schema(description = "Tipo do feedback (ELOGIO, RECLAMACAO, SUGESTAO)", example = "ELOGIO", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O tipo de feedback é obrigatório.") String tipoFeedback,

        @Schema(description = "Nota do feedback (1 a 5)", example = "4", minimum = "1", maximum = "5")
        @Min(value = 1, message = "A nota mínima é 1.")
        @Max(value = 5, message = "A nota máxima é 5.") Integer nota,

        @Schema(description = "Comentário do consumidor", example = "Atendimento excelente, produto substituído rapidamente")
        String comentario,

        @Schema(description = "Data do feedback", example = "01/04/2025", accessMode = Schema.AccessMode.READ_ONLY)
        String dataFeedback,

        @Schema(description = "ID do consumidor que realizou o feedback", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long idConsumidor,

        @Schema(description = "Nome do consumidor", example = "Maria", accessMode = Schema.AccessMode.READ_ONLY)
        String nomeConsumidor,

        @Schema(description = "ID da loja", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O ID da loja é obrigatório.") Long idLoja,

        @Schema(description = "ID da solicitação associada", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O ID da solicitação é obrigatório.") Long idSolicitacao
                          ) {


    public FeedbackDTO(Feedback feedback){
        this(feedback.getIdFeedback(),
                feedback.getTipoFeedback().getDescricao(),
                feedback.getNota(),
                feedback.getComentario(),
                feedback.getDataFeedback()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("pt", "BR"))),
                feedback.getConsumidor().getIdConsumidor(),
                feedback.getConsumidor().getNome().split(" ")[0],
                feedback.getLoja().getIdLoja(),
                feedback.getSolicitacao().getId());
    }
}
