package com.logistica.doisv.services;

import com.logistica.doisv.entities.Consumidor;
import com.logistica.doisv.entities.Produto;
import com.logistica.doisv.entities.Solicitacao;
import com.logistica.doisv.entities.Venda;
import com.logistica.doisv.repositories.ConsumidorRepository;
import com.logistica.doisv.repositories.ProdutoRepository;
import com.logistica.doisv.repositories.SolicitacaoRepository;
import com.logistica.doisv.repositories.VendaRepository;
import jakarta.annotation.PostConstruct;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class ExcelExportService {

    @Autowired
    private ConsumidorRepository consumidorRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Map<String, ConfiguracaoRelatorio<?>> relatorios;

    @PostConstruct
    public void inicializar() {
        relatorios = new LinkedHashMap<>();

        relatorios.put("consumidores", new ConfiguracaoRelatorio<Consumidor>(
                "Consumidores",
                new String[]{"ID", "Nome", "CPF/CNPJ", "E-mail", "Celular", "Telefone", "Endereço", "Status"},
                List.of(
                        c -> c.getIdConsumidor(),
                        c -> c.getNome(),
                        c -> c.getCpf_cnpj(),
                        c -> c.getEmail(),
                        c -> c.getCelular(),
                        c -> c.getTelefone(),
                        c -> c.getEndereco(),
                        c -> c.getStatus() != null ? c.getStatus().getStatusItem() : ""
                )
        ));

        relatorios.put("vendas", new ConfiguracaoRelatorio<Venda>(
                "Vendas",
                new String[]{"ID", "Serial", "Consumidor", "Status do pedido", "Preço total", "Desconto", "Forma de pagamento", "Data de criação", "Data de entrega", "Status"},
                List.of(
                        v -> v.getId(),
                        v -> v.getSerialVenda(),
                        v -> v.getConsumidor() != null ? v.getConsumidor().getNome() : "",
                        v -> v.getStatusPedido() != null ? v.getStatusPedido().getStatusPedido() : "",
                        v -> v.getPrecoTotal(),
                        v -> v.getDesconto(),
                        v -> v.getFormaPagamento(),
                        v -> v.getDataCriacao() != null ? FORMATO_DATA_HORA.format(v.getDataCriacao().atZone(ZoneId.systemDefault())) : "",
                        v -> v.getDataEntrega() != null ? FORMATO_DATA.format(v.getDataEntrega()) : "",
                        v -> v.getStatus() != null ? v.getStatus().getStatusItem() : ""
                )
        ));

        relatorios.put("solicitacoes", new ConfiguracaoRelatorio<Solicitacao>(
                "Solicitações",
                new String[]{"ID", "Tipo", "Consumidor", "Serial da venda", "Produto", "Quantidade", "Motivo", "Status da solicitação", "Data da solicitação", "Última atualização"},
                List.of(
                        s -> s.getId(),
                        s -> s.getTipoSolicitacao() != null ? s.getTipoSolicitacao().getDescricao() : "",
                        s -> s.getConsumidor() != null ? s.getConsumidor().getNome() : "",
                        s -> s.getVenda() != null ? s.getVenda().getSerialVenda() : "",
                        s -> s.getItemVenda() != null && s.getItemVenda().getProduto() != null ? s.getItemVenda().getProduto().getDescricao() : "",
                        s -> s.getQuantidade(),
                        s -> s.getMotivo(),
                        s -> s.getStatusSolicitacao() != null ? s.getStatusSolicitacao().getStatusSolicitacao() : "",
                        s -> s.getDataSolicitacao() != null ? FORMATO_DATA_HORA.format(s.getDataSolicitacao().atZone(ZoneId.systemDefault())) : "",
                        s -> s.getDataAtualizacao() != null ? FORMATO_DATA_HORA.format(s.getDataAtualizacao()) : ""
                )
        ));

        relatorios.put("produtos", new ConfiguracaoRelatorio<Produto>(
                "Produtos",
                new String[]{"ID", "Descrição", "Unidade de medida", "Preço", "Status"},
                List.of(
                        p -> p.getIdProduto(),
                        p -> p.getDescricao(),
                        p -> p.getUnidadeMedida(),
                        p -> p.getPreco(),
                        p -> p.getStatus() != null ? p.getStatus().getStatusItem() : ""
                )
        ));
    }

    public byte[] exportar(String nomeRelatorio, Long idLoja) {
        ConfiguracaoRelatorio<?> configuracao = relatorios.get(nomeRelatorio.toLowerCase());

        if (configuracao == null) {
            throw new IllegalArgumentException("Relatório não encontrado: " + nomeRelatorio);
        }

        List<?> dados = buscarDados(nomeRelatorio.toLowerCase(), idLoja);

        try (XSSFWorkbook planilha = new XSSFWorkbook();
             ByteArrayOutputStream saida = new ByteArrayOutputStream()) {

            gerar(planilha, configuracao, dados);
            planilha.write(saida);
            return saida.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar relatório Excel: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void gerar(XSSFWorkbook planilha, ConfiguracaoRelatorio<T> configuracao, List<?> dados) {
        Sheet aba = planilha.createSheet(configuracao.nomeAba);
        CellStyle estiloCabecalho = criarEstiloCabecalho(planilha);

        criarCabecalho(aba, configuracao.colunas, estiloCabecalho);
        criarLinhas(aba, (List<T>) dados, configuracao.extratores);

        for (int i = 0; i < configuracao.colunas.length; i++) {
            aba.autoSizeColumn(i);
        }
    }

    private void criarCabecalho(Sheet aba, String[] colunas, CellStyle estilo) {
        Row linhaCabecalho = aba.createRow(0);
        for (int i = 0; i < colunas.length; i++) {
            Cell celula = linhaCabecalho.createCell(i);
            celula.setCellValue(colunas[i]);
            celula.setCellStyle(estilo);
        }
    }

    private <T> void criarLinhas(Sheet aba, List<T> dados, List<Function<T, Object>> extratores) {
        for (int i = 0; i < dados.size(); i++) {
            Row linha = aba.createRow(i + 1);
            T item = dados.get(i);
            for (int j = 0; j < extratores.size(); j++) {
                Object valor = extratores.get(j).apply(item);
                preencherCelula(linha.createCell(j), valor);
            }
        }
    }

    private void preencherCelula(Cell celula, Object valor) {
        if (valor == null) {
            celula.setCellValue("");
        } else if (valor instanceof Number numero) {
            celula.setCellValue(numero.doubleValue());
        } else {
            celula.setCellValue(valor.toString());
        }
    }

    private CellStyle criarEstiloCabecalho(XSSFWorkbook planilha) {
        CellStyle estilo = planilha.createCellStyle();

        Font fonte = planilha.createFont();
        fonte.setBold(true);
        fonte.setColor(IndexedColors.WHITE.getIndex());
        estilo.setFont(fonte);

        estilo.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return estilo;
    }

    private List<?> buscarDados(String nomeRelatorio, Long idLoja) {
        return switch (nomeRelatorio) {
            case "consumidores" -> consumidorRepository.buscarTodosPorLoja(idLoja);
            case "vendas" -> vendaRepository.buscarTodasPorLoja(idLoja);
            case "solicitacoes" -> solicitacaoRepository.buscarTodasPorLoja(idLoja);
            case "produtos" -> produtoRepository.buscarTodosPorLoja(idLoja);
            default -> throw new IllegalArgumentException("Relatório não encontrado: " + nomeRelatorio);
        };
    }

    private record ConfiguracaoRelatorio<T>(
            String nomeAba,
            String[] colunas,
            List<Function<T, Object>> extratores
    ) {}
}
