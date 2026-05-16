package com.logistica.doisv.modules.solicitacao.entity;

import com.logistica.doisv.core.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_Anexo_Solicitacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnexoSolicitacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String urlImagem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status = Status.ATIVO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSolicitacao", nullable = false)
    private Solicitacao solicitacao;
}
