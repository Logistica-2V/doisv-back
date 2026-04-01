package com.logistica.doisv.services;

import com.logistica.doisv.dto.registro_venda.requisicao.ItemDTO;
import com.logistica.doisv.dto.registro_venda.requisicao.RegistroVendaDTO;
import com.logistica.doisv.dto.registro_venda.resposta.VendaDTO;
import com.logistica.doisv.dto.registro_venda.resposta.VendaResumidaDTO;
import com.logistica.doisv.entities.*;
import com.logistica.doisv.entities.enums.Status;
import com.logistica.doisv.entities.enums.StatusPedido;
import com.logistica.doisv.repositories.ConsumidorRepository;
import com.logistica.doisv.repositories.ProdutoRepository;
import com.logistica.doisv.repositories.VendaRepository;
import com.logistica.doisv.services.exceptions.RegraNegocioException;
import com.logistica.doisv.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class VendaService {

    @Autowired
    private VendaRepository repository;

    @Autowired
    private ConsumidorRepository consumidorRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private EmailService emailService;

    @Transactional(readOnly = true)
    public Page<VendaResumidaDTO> buscarTodasVendasPorLoja(Pageable pageable, Long idLoja){
        return repository.findVendasResumidasByLojaId(pageable, idLoja);
    }

    @Transactional(readOnly = true)
    public VendaDTO buscarPorId(Long idVenda, Long idLoja){
        Venda venda = repository.findByIdAndLojaIdLoja(idVenda, idLoja)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));

        return new VendaDTO(venda);
    }

    @Transactional
    public VendaDTO salvar(RegistroVendaDTO dto, Long idLoja){
        Consumidor consumidor = consumidorRepository.buscarConsumidorAtivo(dto.idConsumidor(), idLoja)
                .orElseThrow(() -> new ResourceNotFoundException("Consumidor não encontrado"));

        Loja loja = consumidor.getLoja();
        List<Produto> produtos = buscarProdutos(dto.itensVenda(), idLoja);
        StatusPedido statusPedido = StatusPedido.converterDeStringParaEnum(dto.statusPedido());

        Venda venda = Venda.criar(consumidor, loja, statusPedido, dto.formaPagamento(), dto.desconto(),
                dto.prazoTroca(), dto.prazoDevolucao());

        adicionarItensNaVenda(venda, produtos, dto.itensVenda());

        gerarAcessoConsumidor(venda);

        repository.save(venda);

        return new VendaDTO(venda);
    }

    @Transactional
    public VendaDTO atualizar(Long idVenda, RegistroVendaDTO dto, Long idLoja){
        Venda venda = buscarVendaParaAtualizacao(idVenda, idLoja);

        atualizarConsumidorSeNecessario(venda, dto.idConsumidor(), idLoja);
        List<Produto> produtos = buscarProdutos(dto.itensVenda(), idLoja);
        StatusPedido statusPedido = StatusPedido.converterDeStringParaEnum(dto.statusPedido());

        venda.atualizarCampos(statusPedido, dto.formaPagamento(), dto.desconto(),
                dto.prazoTroca(), dto.prazoDevolucao());

        adicionarItensNaVenda(venda, produtos, dto.itensVenda());

        repository.save(venda);
        gerarAcessoConsumidor(venda);

        return new VendaDTO(venda);
    }

    @Transactional
    public void inativar(List<Long> idsVendas, Long idLoja){
        List<StatusPedido> statusBloqueadosParaInativar = List.of(StatusPedido.ENTREGUE, StatusPedido.CANCELADA);

        int quantidadeVendasInativadas = repository.atualizarStatusVendas(idsVendas, idLoja, Status.INATIVO,
                statusBloqueadosParaInativar);

        if(quantidadeVendasInativadas <= 0){
            throw new RegraNegocioException("Nenhuma venda elegível para inativação. " +
                    "Vendas com status ENTREGUE ou CANCELADA não podem ser inativadas.");
        }
    }

    private List<Produto> buscarProdutos(List<ItemDTO> itensVendaDTO, Long idLoja){
        List<Long> idsProdutosParaBusca = itensVendaDTO.stream()
                .filter(item -> item.idItemVenda() == null)
                .map(ItemDTO::idProduto)
                .toList();

        return produtoRepository.buscarProdutoParaVenda(idsProdutosParaBusca, idLoja);
    }

    private void adicionarItensNaVenda(Venda venda, List<Produto> produtos, List<ItemDTO> itensDTO){
        Map<Long, Produto> produtosPorId = produtos.stream()
                .collect(Collectors.toMap(Produto::getIdProduto, Function.identity()));

        List<Long> idsParaManter = itensDTO.stream()
                .map(ItemDTO::idItemVenda)
                .filter(Objects::nonNull)
                .toList();

        venda.removerItens(idsParaManter);

        for (ItemDTO item : itensDTO) {
            if(item.idItemVenda() == null){
                Produto produto = produtosPorId.get(item.idProduto());

                if (produto == null) {
                    throw new ResourceNotFoundException("Produto não encontrado: " + item.idProduto());
                }
                produto.validarAtivo();

                venda.adicionarItem(ItemVenda.criar(produto.getPreco(), item.valorVendido(), item.quantidade(), item.detalhe(),
                        venda, produto));
            }else{
                venda.atualizarItem(item.idItemVenda(), item.valorVendido(), item.quantidade(), item.detalhe());
            }
        }
        venda.calcularPrecoTotal();
    }

    private void gerarAcessoConsumidor(Venda venda){
        if(venda.getStatusPedido() == StatusPedido.ENTREGUE){
            venda.setSerialVenda(UUID.randomUUID().toString().substring(0,11).replace("-", ""));
            var senha = venda.getConsumidor().getCpf_cnpj().substring(0,4) + "@" + LocalDate.now().getYear();
            venda.setSenha(encoder.encode(senha));
            venda.setDataEntrega(LocalDate.now());

            enviarAcessoConsumidor(venda, senha);
        }
        else if(venda.getStatusPedido() == StatusPedido.CANCELADA){
            venda.setSerialVenda(null);
            venda.setSenha(null);
            venda.setStatus(Status.INATIVO);
        }
    }

    private void enviarAcessoConsumidor(Venda venda, String senha){
        if(venda.getStatusPedido().equals(StatusPedido.ENTREGUE)) {
            emailService.enviarEmailAcessoConsumidor(venda, senha);
        }
    }

    private Venda buscarVendaParaAtualizacao(Long idVenda, Long idLoja) {
        return repository.findByIdAndLojaIdLoja(idVenda, idLoja)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada."));
    }

    private void atualizarConsumidorSeNecessario(Venda venda, Long idConsumidor, Long idLoja) {
        if (venda.pertenceAoConsumidor(idConsumidor)){ return;}

        Consumidor novoConsumidor = consumidorRepository.buscarConsumidorAtivo(idConsumidor, idLoja)
                    .orElseThrow(() -> new ResourceNotFoundException("Consumidor não localizado."));

        venda.atualizarConsumidor(novoConsumidor);
    }
}
