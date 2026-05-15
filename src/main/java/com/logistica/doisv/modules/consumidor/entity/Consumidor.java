package com.logistica.doisv.modules.consumidor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logistica.doisv.modules.solicitacao.entity.Solicitacao;
import com.logistica.doisv.modules.venda.entity.Venda;
import com.logistica.doisv.core.enums.Status;
import com.logistica.doisv.modules.feedback.entity.Feedback;
import com.logistica.doisv.modules.loja.entity.Loja;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "tb_Consumidor", uniqueConstraints = @UniqueConstraint(columnNames = {"idLoja", "cpf_cnpj"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Consumidor {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idConsumidor;
    
    @Column(nullable=false)
    private String nome;
    private String cpf_cnpj;
    private String email;

    @Column(nullable=false)
    private String celular;
    private String telefone;
    private String endereco;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ATIVO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "idLoja")
    private Loja loja;

    @OneToMany(mappedBy = "consumidor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Venda> vendas = new ArrayList<>();

    @OneToMany(mappedBy = "consumidor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Solicitacao> solicitacoes = new ArrayList<>();

    @OneToMany(mappedBy = "consumidor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Feedback> feedbacks = new ArrayList<>();

}
