package com.logistica.doisv.services;

import com.logistica.doisv.dto.registro_venda.requisicao.ItemDTO;
import com.logistica.doisv.dto.registro_venda.requisicao.RegistroVendaDTO;
import com.logistica.doisv.dto.registro_venda.resposta.VendaDTO;
import com.logistica.doisv.entities.*;
import com.logistica.doisv.entities.enums.Status;
import com.logistica.doisv.entities.enums.StatusPedido;
import com.logistica.doisv.repositories.ConsumidorRepository;
import com.logistica.doisv.repositories.ProdutoRepository;
import com.logistica.doisv.repositories.VendaRepository;
import com.logistica.doisv.services.exceptions.AssociacaoInvalidaException;
import com.logistica.doisv.services.exceptions.EdicaoNaoPermitidaException;
import com.logistica.doisv.services.exceptions.ResourceNotFoundException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
    private VendaRepository vendaRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private EmailService emailService;

    @Transactional(readOnly = true)
    public Page<VendaDTO> buscarTodasVendasPorLoja(Pageable pageable, Long idLoja){
        return repository.findAllByLoja_idLoja(pageable, idLoja).map(VendaDTO::new);
    }

    @Transactional(readOnly = true)
    public VendaDTO buscarPorId(Long id, Long idLoja){
        Venda venda = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));
        if (venda.getLoja().getIdLoja().equals(idLoja)) {
            return new VendaDTO(venda);
        }
        throw new AssociacaoInvalidaException("Não foi possível buscar a venda com esse ID");
    }

    @Transactional
    public VendaDTO salvar(RegistroVendaDTO dto, Long idLoja) throws MessagingException {
        Consumidor consumidor = consumidorRepository.findById(dto.idConsumidor()).orElseThrow(() -> new ResourceNotFoundException("Consumidor não encontrado"));

        if(consumidor.getLoja().getIdLoja().equals(idLoja)){
            Loja loja = consumidor.getLoja();

            Venda venda = new Venda(loja, consumidor, dto.statusPedido(), dto.desconto(), dto.formaPagamento(), dto.prazoTroca(), dto.prazoDevolucao());

            calcularValorVenda(venda, dto, loja);
            gerarAcessoConsumidor(venda);
            VendaDTO vendaDTO = new VendaDTO(repository.save(venda));

            if(venda.getStatusPedido().equals(StatusPedido.ENTREGUE)){
                emailService.enviarEmailAcessoConsumidor(venda);
            }

            return vendaDTO;
        }
        else{
            throw new AssociacaoInvalidaException("O consumidor não está associado a esta loja");
        }
    }

    @Transactional
    public VendaDTO atualizar(Long idVenda, RegistroVendaDTO dto, Long idLoja) throws MessagingException {
        Venda venda = repository.findById(idVenda).orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));

        if(venda.getStatusPedido() == StatusPedido.ENTREGUE || venda.getStatusPedido() == StatusPedido.CANCELADA){
            throw new EdicaoNaoPermitidaException("Status atual da venda não permite edição de dados");
        }

        if(!venda.getConsumidor().getIdConsumidor().equals(dto.idConsumidor())){
            venda.setConsumidor(consumidorRepository.findById(dto.idConsumidor())
                            .filter(c -> c.getLoja().getIdLoja().equals(idLoja))
                            .orElseThrow(() -> new RuntimeException("Consumidor não localizado ou não associado a esta loja")));
        }
        venda.setDesconto(dto.desconto());
        venda.setPrazoTroca(dto.prazoTroca());
        venda.setPrazoDevolucao(dto.prazoDevolucao());
        venda.setStatusPedido(StatusPedido.converterParaString(dto.statusPedido()));
        venda.getItensVenda().clear();
        calcularValorVenda(venda, dto, venda.getLoja());
        gerarAcessoConsumidor(venda);
        VendaDTO vendaDTO = new VendaDTO(repository.save(venda));

        if(venda.getStatusPedido().equals(StatusPedido.ENTREGUE)){
            emailService.enviarEmailAcessoConsumidor(venda);
        }
        return vendaDTO;
    }

    @Transactional
    public void inativar(List<Long> ids, Long idLoja){
        List<Venda> vendas = repository.findAllById(ids);
        if(vendas.stream().anyMatch(v -> !v.getLoja().getIdLoja().equals(idLoja))){
            throw new AssociacaoInvalidaException("Você não tem permissão para editar um ou mais produtos desta lista.");
        }
        vendas.forEach(v -> v.setStatus(Status.INATIVO));
        repository.saveAll(vendas);
    }

    private void gerarAcessoConsumidor(Venda venda){
        if(venda.getStatusPedido() == StatusPedido.ENTREGUE){
            venda.setSerialVenda(UUID.randomUUID().toString().substring(0,11).replace("-", ""));
            var senha = venda.getConsumidor().getCpf_cnpj().substring(0,4) + "@" + LocalDate.now().getYear(); //
            venda.setSenha(encoder.encode(senha));
            venda.setDataEntrega(LocalDate.now());
        }
        else if(venda.getStatusPedido() == StatusPedido.CANCELADA){
            venda.setSerialVenda(null);
            venda.setSenha(null);
            venda.setStatus(Status.INATIVO);
        }
    }

    private void calcularValorVenda(Venda venda, RegistroVendaDTO dto, Loja loja){
        venda.setPrecoTotal(BigDecimal.valueOf(0));
        List<Produto> produtos = produtoRepository.findAllById(dto.itensVenda().stream().map(ItemDTO::idProduto).toList());

        if (produtos.stream().anyMatch(p -> !p.getLoja().getIdLoja().equals(loja.getIdLoja()))){
            throw new AssociacaoInvalidaException("Um ou mais produtos desta lista não está associado a esta loja");
        }

        Map<Long, Produto> produtosPorId = produtos.stream().collect(Collectors.toMap(Produto::getIdProduto, Function.identity()));

        for(ItemDTO i : dto.itensVenda()){
            Produto produto = produtosPorId.get(i.idProduto());
            ItemVenda item = new ItemVenda(produto.getPreco(), i.valorVendido(), i.quantidade(), i.detalhe(), venda, produto);
            venda.getItensVenda().add(item);

            venda.setPrecoTotal(venda.getPrecoTotal()
                    .add(item.getPrecoVendido()
                            .multiply(BigDecimal.valueOf(item.getQuantidade())))
                    .setScale(2, RoundingMode.HALF_UP));
        }
        venda.setPrecoTotal(venda.getPrecoTotal().subtract(venda.getDesconto()));
    }
}
