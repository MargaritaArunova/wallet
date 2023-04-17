package com.wallet.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "Currency")
@SequenceGenerator(allocationSize = 1, name = "currency_seq", sequenceName = "currency_seq")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_seq")
    Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Column(name = "full_description", nullable = false)
    private String fullDescription;

    @Column(name = "short_description", nullable = false)
    private String shortDescription;

    @Column(name = "value", nullable = false)
    private BigDecimal value;
}
