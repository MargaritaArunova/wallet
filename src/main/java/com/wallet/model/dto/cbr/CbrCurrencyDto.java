package com.wallet.model.dto.cbr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@Accessors(chain = true)
@XmlRootElement(name = "Valute")
@XmlAccessorType(XmlAccessType.FIELD)
public class CbrCurrencyDto {

    @XmlElement(name = "NumCode")
    private String numCode;

    @XmlElement(name = "CharCode")
    private String charCode;

    @XmlElement(name = "Nominal")
    private int nominal;

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "Value")
    @JsonIgnore
    @Transient
    private String value;

    private BigDecimal currencyValue;
}
