package com.logistica.doisv.modules.produto.entity;

import com.logistica.doisv.core.enums.Status;
import com.logistica.doisv.core.exception.RegraNegocioException;
import com.logistica.doisv.modules.loja.entity.Loja;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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

    @Column(nullable = false, length = 160)
    private String descricao;

    @Column(nullable = false, length = 10)
    private String unidadeMedida;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal preco;

    @Column(length = 120)
    private String imagem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status = Status.ATIVO;

    @ManyToOne
    @JoinColumn(name = "idLoja", nullable = false)
    private Loja loja;


    public void validarAtivo() {
        if (this.status.equals(Status.INATIVO)) {
            throw new RegraNegocioException(
                    String.format("Não é possível registrar uma venda com produto inativo: %d - %s",
                            this.idProduto, this.descricao));
        }
    }

}
