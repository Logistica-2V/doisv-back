package com.logistica.doisv.modules.metrica.service;

import com.logistica.doisv.core.enums.MotivoSolicitacao;
import com.logistica.doisv.core.enums.StatusSolicitacao;
import com.logistica.doisv.core.enums.TipoFeedback;
import com.logistica.doisv.core.enums.TipoSolicitacao;
import com.logistica.doisv.core.exception.RegraNegocioException;
import com.logistica.doisv.core.util.conversion.PaginacaoUtil;
import com.logistica.doisv.core.util.generation.DataUtil;
import com.logistica.doisv.modules.feedback.entity.Feedback;
import com.logistica.doisv.modules.feedback.repository.FeedbackRepository;
import com.logistica.doisv.modules.loja.entity.Loja;
import com.logistica.doisv.modules.loja.repository.LojaRepository;
import com.logistica.doisv.modules.metrica.dto.MetricasPrivadasDTO;
import com.logistica.doisv.modules.metrica.dto.MetricasPublicasLojaDTO;
import com.logistica.doisv.modules.metrica.dto.ProdutoDashboardDTO;
import com.logistica.doisv.modules.produto.entity.Produto;
import com.logistica.doisv.modules.solicitacao.dto.SolicitacaoResumidaDTO;
import com.logistica.doisv.modules.solicitacao.entity.Solicitacao;
import com.logistica.doisv.modules.solicitacao.repository.SolicitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MetricaService {
    private static final String TIPO_LOJA = "loja";
    private static final String TIPO_SOLICITACAO = "solicitacao";

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private LojaRepository lojaRepository;

    public MetricasPrivadasDTO metricasPrivadasPorLojaEPeriodo(Long idLoja, Integer periodo){
        validarParametros(idLoja, periodo);

        LocalDate inicio = DataUtil.hoje().minusDays(periodo);
        LocalDate fim = DataUtil.hoje();

        List<Solicitacao> solicitacoes = solicitacaoRepository.buscarSolicitacaoPorLojaEPeriodo(idLoja, inicio, fim);

        if (solicitacoes.isEmpty()) {
            throw new RegraNegocioException("Nenhuma avaliação encontrada para o período informado");
        }

        List<Feedback> feedbacks = solicitacoes.stream()
                .map(Solicitacao::getFeedbacks)
                .filter(f -> f != null && !f.isEmpty())
                .flatMap(Set::stream)
                .toList();

        return MetricasPrivadasDTO.builder()
                .totalSolicitacoesAvaliadas(obterQuantidadeSolicitacoesAvaliadas(feedbacks))
                .notaMedia(gerarAvaliacoes(feedbacks))
                .taxaConclusaoGeral(calcularTaxaConclusaoGeral(solicitacoes))
                .quantidadeSolicitacoesPorStatus(obterQuantidadeSolicitacoesPorStatus(solicitacoes))
                .quantidadeSolicitacoesPorMotivo(obterQuantidadeSolicitacoesPorMotivo(solicitacoes))
                .top5ProdutosComMaisSolicitacoes(buscarTop5ProdutosComMaisSolicitacoes(solicitacoes))
                .solicitacoesPendentes(obterSolicitacoesPendentes(solicitacoes))
                .quantidadeFeedbacksPorNota(calcularQuantidadeFeedbackPorNota(feedbacks))
                .build();
    }

    public Page<MetricasPublicasLojaDTO> metricasPublicasTodasLojas(Pageable pageable, Integer periodo){
        LocalDate inicio = LocalDate.now().minusDays(periodo);
        LocalDate fim = LocalDate.now();

        Page<Loja> lojas = lojaRepository.findAll(pageable);

        List<Feedback> feedbacks = feedbackRepository.buscarFeedbacksPorPeriodo(inicio, fim);

        Map<Long, List<Feedback>> feedbacksPorLoja = feedbacks.stream()
                .collect(Collectors.groupingBy(f -> f.getLoja().getIdLoja()));

        return PaginacaoUtil.paraPagina(pageable, lojas.stream()
                .map(l -> {
                    List<Feedback> feedbacksDaLoja =
                            feedbacksPorLoja.getOrDefault(l.getIdLoja(), Collections.emptyList());

                    return new MetricasPublicasLojaDTO(
                            l.getIdPublico(),
                            l.getNome(),
                            l.getLogo(),
                            l.getSegmento(),
                            feedbacksDaLoja.size(),
                            gerarAvaliacoes(feedbacksDaLoja)
                    );
                }).toList());
    }

    private void validarParametros(Long idLoja, Integer periodo) {
        if (idLoja == null) {
            throw new IllegalArgumentException("ID da loja não pode ser nulo");
        }
        if (periodo == null || periodo <= 0) {
            throw new IllegalArgumentException("Período deve ser maior que zero");
        }
    }

    private Integer obterQuantidadeSolicitacoesAvaliadas(List<Feedback> feedbacks) {
        return (int) feedbacks.stream().map(Feedback::getSolicitacao).distinct().count();
    }

    private Map<String, BigDecimal> gerarAvaliacoes(List<Feedback> feedbacks){
        return Map.of(
                TIPO_LOJA, calcularMediaFeedbacks(feedbacks, TipoFeedback.LOJA),
                TIPO_SOLICITACAO, calcularMediaFeedbacks(feedbacks, TipoFeedback.SOLICITACAO));
    }

    private BigDecimal calcularMediaFeedbacks(List<Feedback> feedbacks, TipoFeedback tipoFeedback){
        List<Feedback> feedbacksFiltrados =  feedbacks.stream()
                .filter(f -> f.getTipoFeedback() == tipoFeedback).toList();

        if(feedbacks.isEmpty() || feedbacksFiltrados.isEmpty()){
            return BigDecimal.ZERO;
        }

        double somaNota = feedbacksFiltrados.stream().mapToDouble(Feedback::getNota).sum();

        return BigDecimal.valueOf(somaNota)
                .divide(BigDecimal.valueOf(feedbacksFiltrados.size()), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularTaxaConclusaoGeral(List<Solicitacao> solicitacoes){
        int concluidas = solicitacoes.stream()
                .filter(s -> s.getStatusSolicitacao() == (StatusSolicitacao.CONCLUIDA))
                .toList()
                .size();

        return calcularPercentual(concluidas, solicitacoes.size());
    }

    private Map<String, Integer> obterQuantidadeSolicitacoesPorStatus(List<Solicitacao> solicitacoes){
        Map<String, Long> contagemPorStatus = solicitacoes.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getStatusSolicitacao().name().toLowerCase(),
                        Collectors.counting()
                ));

        Map<String, Integer> resultado = new HashMap<>();
        for(StatusSolicitacao status : StatusSolicitacao.values()){
            String chave = status.name().toLowerCase();
            resultado.put(chave, contagemPorStatus.getOrDefault(chave, 0L).intValue());
        }

        return resultado;
    }

    private Map<String, Integer> obterQuantidadeSolicitacoesPorMotivo(List<Solicitacao> solicitacoes){
        Map<String, Long> contagemPorMotivo = solicitacoes.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getMotivo().getDescricao().toLowerCase(),
                        Collectors.counting()
                ));

        Map<String, Integer> resultado = new HashMap<>();
        for(MotivoSolicitacao motivo : MotivoSolicitacao.values()){
            String chave = motivo.getDescricao().toLowerCase();
            resultado.put(chave, contagemPorMotivo.getOrDefault(chave, 0L).intValue());
        }

        return resultado;
    }

    private List<ProdutoDashboardDTO> buscarTop5ProdutosComMaisSolicitacoes(List<Solicitacao> solicitacoes) {
        if (solicitacoes == null || solicitacoes.isEmpty()) {
            return Collections.emptyList();
        }

        return solicitacoes.stream()
                .filter(s -> s.getItemVenda() != null)
                .filter(s -> s.getItemVenda().getProduto() != null)
                .collect(Collectors.groupingBy(s -> s.getItemVenda().getProduto().getIdProduto()))
                .values()
                .stream()
                .map(this::criarProdutoDashboardDTO)
                .sorted(Comparator.comparing(ProdutoDashboardDTO::totalSolicitacoes).reversed())
                .limit(5)
                .toList();
    }

    private List<SolicitacaoResumidaDTO> obterSolicitacoesPendentes(List<Solicitacao> solicitacoes){
        return solicitacoes.stream()
                .filter(s -> s.getStatusSolicitacao() == StatusSolicitacao.PENDENTE)
                .map(SolicitacaoResumidaDTO::new)
                .toList();
    }

    private Map<String, Map<Integer, Integer>> calcularQuantidadeFeedbackPorNota(List<Feedback> feedbacks) {
        return Map.of(
                TIPO_LOJA, contarNotasPorTipFeedback(feedbacks, TipoFeedback.LOJA),
                TIPO_SOLICITACAO, contarNotasPorTipFeedback(feedbacks, TipoFeedback.SOLICITACAO)
        );
    }

    private Map<Integer, Integer> contarNotasPorTipFeedback(List<Feedback> feedbacks, TipoFeedback tipoFeedback) {
        Map<Integer, Integer> resultado = new HashMap<>();

        for (int nota = 1; nota <= 5; nota++) {
            resultado.put(nota, 0);
        }

        feedbacks.stream()
                .filter(f -> f.getTipoFeedback() == tipoFeedback)
                .forEach(f -> resultado.put(f.getNota(), resultado.get(f.getNota()) + 1));

        return resultado;
    }

    private BigDecimal calcularPercentual(long parte, long total) {
        if (total == 0) {
            return BigDecimal.ZERO;
        }
        double valor = (double) parte / total * 100.0;
        return BigDecimal.valueOf(valor).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    private ProdutoDashboardDTO criarProdutoDashboardDTO(List<Solicitacao> solicitacoesProduto) {
        Produto produto = solicitacoesProduto.get(0).getItemVenda().getProduto();

        int trocas = contarPorTipo(solicitacoesProduto, TipoSolicitacao.TROCA);
        int devolucoes = contarPorTipo(solicitacoesProduto, TipoSolicitacao.DEVOLUCAO);
        int total = trocas + devolucoes;

        return new ProdutoDashboardDTO(
                produto.getDescricao(),
                trocas,
                devolucoes,
                total,
                calcularNivelAtencao(total)
        );
    }

    private int contarPorTipo(List<Solicitacao> solicitacoes, TipoSolicitacao tipo) {
        return (int) solicitacoes.stream()
                .filter(s -> s.getTipoSolicitacao() == tipo)
                .count();
    }

    private String calcularNivelAtencao(int totalSolicitacoes) {
        if (totalSolicitacoes >= 4) {
            return "Alto";
        }

        if (totalSolicitacoes >= 2) {
            return "Médio";
        }

        return "Baixo";
    }
}
