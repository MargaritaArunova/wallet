package com.wallet.model.entity;

import com.wallet.model.type.CurrencyType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "Wallet")
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(allocationSize = 1, name = "wallet_seq", sequenceName = "wallet_seq")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallet_seq")
    private Long id;

    @Column(name = "is_hidden", nullable = false)
    private Integer isHidden;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @Column(name = "amount_limit", nullable = false)
    private BigDecimal amountLimit;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "income", nullable = false)
    private BigDecimal income;

    @Column(name = "spendings", nullable = false)
    private BigDecimal spendings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "wallet", cascade = CascadeType.ALL)
    private List<Operation> operations = new ArrayList<>();

}
