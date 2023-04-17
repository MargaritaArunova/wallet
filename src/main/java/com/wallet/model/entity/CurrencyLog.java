package com.wallet.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "currency_log")
@SequenceGenerator(allocationSize = 1, name = "currency_log_seq", sequenceName = "currency_log_seq")
public class CurrencyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_log_seq")
    private Long id;

    @CreationTimestamp
    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "usd", nullable = false)
    private BigDecimal usd;

    @Column(name = "eur", nullable = false)
    private BigDecimal eur;

    @Column(name = "chf", nullable = false)
    private BigDecimal chf;

    @Column(name = "gbp", nullable = false)
    private BigDecimal gbp;

    @Column(name = "jpy", nullable = false)
    private BigDecimal jpy;

    @Column(name = "sek", nullable = false)
    private BigDecimal sek;
}
