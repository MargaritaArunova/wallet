package com.wallet.model.entity;

import com.wallet.model.type.TransactionType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "operation")
@SequenceGenerator(allocationSize = 1, name = "operation_seq", sequenceName = "operation_seq")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "operation_seq")
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @CreationTimestamp
    @Column(name = "date", nullable = false)
    private Instant date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
