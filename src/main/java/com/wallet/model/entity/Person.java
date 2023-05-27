package com.wallet.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "person")
@SequenceGenerator(allocationSize = 1, name = "person_seq", sequenceName = "person_seq")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_seq")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "income", nullable = false)
    private BigDecimal income;

    @Column(name = "spendings", nullable = false)
    private BigDecimal spendings;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = CascadeType.ALL)
    private List<Wallet> wallets = new ArrayList<>();
}
