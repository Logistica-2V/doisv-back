package com.logistica.doisv.modules.loja.entity;

    import com.logistica.doisv.core.enums.Status;
    import jakarta.persistence.*;
    import lombok.*;
    import org.hibernate.annotations.JdbcTypeCode;
    import org.hibernate.type.SqlTypes;

    import java.time.LocalDate;
    import java.util.UUID;

    @Entity
    @Table(name = "tb_licenca")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Licenca {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @JdbcTypeCode(SqlTypes.CHAR)
        @Column(nullable = false, length = 36)
        private UUID idLicenca;

        @Column(nullable = false)
        private LocalDate validade;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 10)
        private Status status = Status.ATIVO;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "idLoja", nullable = false)
        private Loja loja;


        public void setStatus(Status status){
            this.status = status;
            this.loja.setStatus(status);
        }
    }
