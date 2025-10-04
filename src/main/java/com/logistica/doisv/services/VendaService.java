package com.logistica.doisv.services;

import com.logistica.doisv.dto.registro_venda.requisicao.ItemDTO;
import com.logistica.doisv.dto.registro_venda.requisicao.RegistroVendaDTO;
import com.logistica.doisv.dto.registro_venda.resposta.VendaDTO;
import com.logistica.doisv.entities.*;
import com.logistica.doisv.repositories.ConsumidorRepository;
import com.logistica.doisv.repositories.ProdutoRepository;
import com.logistica.doisv.repositories.VendaRepository;
import com.logistica.doisv.services.exceptions.AssociacaoInvalidaException;
import com.logistica.doisv.services.exceptions.DatabaseException;
import com.logistica.doisv.services.exceptions.EdicaoNaoPermitidaException;
import com.logistica.doisv.services.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private PasswordEncoder encoder;

    @Transactional(readOnly = true)
    public Page<VendaDTO> buscarTodasVendasPorLoja(Pageable pageable, Long idLoja){
        return repository.findAllByLoja_idLoja(pageable, idLoja).map(VendaDTO::new);
    }

    @Transactional(readOnly = true)
    public VendaDTO buscarPorId(Long id){
        Venda venda = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));
        return new VendaDTO(venda);
    }

    @Transactional
    public VendaDTO salvar(RegistroVendaDTO dto, Long idLoja){
        Consumidor consumidor = consumidorRepository.findById(dto.idConsumidor()).orElseThrow(() -> new ResourceNotFoundException("Consumidor não encontrado"));

        if(consumidor.getLoja().getIdLoja().equals(idLoja)){
            Loja loja = consumidor.getLoja();
            List<Produto> produtos = produtoRepository.findAllById(dto.itensVenda().stream().map(ItemDTO::idProduto).toList());

            if (produtos.stream().anyMatch(p -> !p.getLoja().getIdLoja().equals(loja.getIdLoja()))){
                throw new AssociacaoInvalidaException("Um ou mais produtos desta lista não está associado a esta loja");
            }

            Venda venda = new Venda(loja, consumidor, dto.statusPedido(), dto.desconto(), dto.formaPagamento(), dto.prazoTroca(), dto.prazoDevolucao());
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
            gerarAcessoConsumidor(venda);
            return new VendaDTO(repository.save(venda));
        }
        else{
            throw new AssociacaoInvalidaException("O consumidor não está associado a esta loja");
        }
    }

    public VendaDTO atualizar(Long idVenda, RegistroVendaDTO dto, Long idLoja){
        Venda venda = repository.findById(idVenda).orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));

        if(venda.getStatusPedido() == StatusPedido.ENTREGUE || venda.getStatusPedido() == StatusPedido.CANCELADA){
            //Criar uma validação personalizada
            throw new EdicaoNaoPermitidaException("Status atual da venda não permite edição de dados");
        }

        try{
            repository.delete(venda);
            return salvar(dto, idLoja);
        }catch (DataIntegrityViolationException e){
            throw new DatabaseException("Falha na integridade referencial");
        }
    }

    @Transactional
    public void inativar(List<Long> ids, Long idLoja){
        List<Venda> vendas = repository.findAllById(ids);
        if(vendas.stream().anyMatch(v -> !v.getLoja().getIdLoja().equals(idLoja))){
            throw new AssociacaoInvalidaException("Você não tem permissão para editar um ou mais produtos desta lista.");
        }
        vendas.forEach(v -> v.setStatus(Status.INATVO));
        repository.saveAll(vendas);
    }

    private void gerarAcessoConsumidor(Venda venda){
        if(venda.getStatusPedido() == StatusPedido.ENTREGUE){
            venda.setSerialVenda(UUID.randomUUID().toString().substring(0,11).replace("-", ""));
            var senha = venda.getConsumidor().getCpf_cnpj().substring(0,4) + "@" + UUID.randomUUID().toString().substring(0,3); //
            venda.setSenha(encoder.encode(senha));
            venda.setDataEntrega(LocalDate.now());
        }
        else if(venda.getStatusPedido() == StatusPedido.CANCELADA){
            venda.setSerialVenda(null);
            venda.setSenha(null);
            venda.setStatus(Status.INATVO);
        }
    }
}
