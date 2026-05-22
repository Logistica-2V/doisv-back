package com.logistica.doisv.modules.feedback.service;

import com.logistica.doisv.core.util.generation.DataUtil;
import com.logistica.doisv.modules.autenticacao.dto.AcessoDTO;
import com.logistica.doisv.modules.consumidor.entity.Consumidor;
import com.logistica.doisv.modules.loja.entity.Loja;
import com.logistica.doisv.modules.solicitacao.entity.Solicitacao;
import com.logistica.doisv.core.enums.TipoFeedback;
import com.logistica.doisv.modules.feedback.dto.FeedbackDTO;
import com.logistica.doisv.modules.feedback.dto.FeedbackResumidoDTO;
import com.logistica.doisv.modules.feedback.entity.Feedback;
import com.logistica.doisv.modules.feedback.repository.FeedbackRepository;
import com.logistica.doisv.modules.consumidor.repository.ConsumidorRepository;
import com.logistica.doisv.modules.loja.repository.LojaRepository;
import com.logistica.doisv.modules.solicitacao.repository.SolicitacaoRepository;
import com.logistica.doisv.core.exception.ResourceNotFoundException;
import com.logistica.doisv.core.util.conversion.PaginacaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository repository;

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private ConsumidorRepository consumidorRepository;

    @Autowired
    private LojaRepository lojaRepository;


    @Transactional(readOnly = true)
    public FeedbackDTO buscarPorId(Long idFeedback, Long idLoja){
        Feedback feedback = repository.buscarFeedbackPorId(idFeedback, idLoja)
                .orElseThrow(() -> new ResourceNotFoundException(String
                        .format("Feedback com ID %d não encontrado", idFeedback)));

        return new FeedbackDTO(feedback);
    }

    @Transactional(readOnly = true)
    public List<FeedbackDTO> buscarPorIdSolicitacao(Long idSolicitacao, AcessoDTO acesso){
        List<Feedback> feedbacks = repository.buscarFeedbacksPorIdSolicitacao(idSolicitacao, acesso.getIdLoja());

        if(feedbacks.isEmpty()){
            throw new ResourceNotFoundException(String.format("Solicitação de ID %d não encontrada.", idSolicitacao));
        }

        if(acesso.getIdConsumidor() != null){
            validarAcessoConsumidor(feedbacks, acesso.getIdConsumidor(), acesso.getIdVenda(), idSolicitacao);
        }

        return feedbacks.stream().map(FeedbackDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public Page<FeedbackDTO> buscarTodosPorLoja(Long idLoja, Integer periodo, Pageable pageable) {
        LocalDate inicio = DataUtil.hoje().minusDays(periodo);
        LocalDate fim = DataUtil.hoje();

        return repository.buscarFeedbacksPaginadosPorLojaEPeriodo(idLoja, inicio, fim, pageable)
                .map(FeedbackDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<FeedbackResumidoDTO> buscarTodosPublicosPorLoja(Pageable pageable, UUID idPublicoLoja, Integer periodo){
        LocalDate inicio = DataUtil.hoje().minusDays(periodo);
        LocalDate fim = DataUtil.hoje();

        List<FeedbackResumidoDTO> feedbacks = repository.buscarFeedbacksPublicosPorLojaEPeriodo(idPublicoLoja, inicio, fim)
                .stream()
                .map(f -> new FeedbackResumidoDTO(null,
                        null,
                        f.getComentario(),
                        f.getNota(),
                        f.getDataFeedback().
                                format(DateTimeFormatter
                                .ofPattern("dd/MM/yyyy", new Locale("pt", "BR")))))
                .toList();

        return PaginacaoUtil.paraPagina(pageable, feedbacks);
    }

    @Transactional
    public FeedbackDTO salvar(FeedbackDTO dto, Long idConsumidor, Long idLoja, Long idVenda){
        Solicitacao solicitacao = solicitacaoRepository
                .buscarSolicitacaoPorIdEVendaEConsumidor(dto.idSolicitacao(),idConsumidor, idVenda)
                .orElseThrow(() -> new ResourceNotFoundException(String
                        .format("Solicitação de ID %d não encontrada.", dto.idSolicitacao())));

        Consumidor consumidor = consumidorRepository.getReferenceById(idConsumidor);
        Loja loja = lojaRepository.getReferenceById(idLoja);

        TipoFeedback tipoFeedback = TipoFeedback.deString(dto.tipoFeedback());

        Feedback feedback = Feedback.criar(solicitacao, consumidor, loja, tipoFeedback, dto.nota(), dto.comentario());

        return new FeedbackDTO(repository.save(feedback));
    }

    private void validarAcessoConsumidor(List<Feedback> feedbacks, Long idConsumidor, Long idVenda, Long idSolicitacao){
        boolean consumidorTemAcesso = feedbacks.stream()
                .allMatch(f -> f.getConsumidor() != null &&
                        Objects.equals(f.getConsumidor().getIdConsumidor(), idConsumidor) &&
                        Objects.equals(f.getSolicitacao().getVenda().getId(), idVenda));


        if(!consumidorTemAcesso){
            throw new ResourceNotFoundException(String.format("Solicitação de ID %d não encontrada.", idSolicitacao));
        }
    }
}
