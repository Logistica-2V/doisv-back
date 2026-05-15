package com.logistica.doisv.modules.produto.entity;

import com.logistica.doisv.core.enums.Status;
import com.logistica.doisv.core.exception.RegraNegocioException;
import com.logistica.doisv.modules.loja.entity.Loja;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_Produto", uniqueConstraints = @UniqueConstraint(columnNames = {"idLoja", "descricao"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduto;
    private String descricao;
    private String unidadeMedida;
    @Column(precision = 10, scale = 2)
    private BigDecimal preco;
    private String imagem;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ATIVO;

    @ManyToOne
    @JoinColumn(name = "idLoja")
    private Loja loja;


    public void validarAtivo() {
        if (this.status.equals(Status.INATIVO)) {
            throw new RegraNegocioException(
                    String.format("Não é possível registrar uma venda com produto inativo: %d - %s",
                            this.idProduto, this.descricao));
        }
    }

}
