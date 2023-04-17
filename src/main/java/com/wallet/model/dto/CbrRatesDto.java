package com.wallet.model.dto;


import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "ValCurs")
@XmlAccessorType(XmlAccessType.FIELD)
public class CbrRatesDto implements Serializable {

    @XmlElement(name = "Valute")
    private List<CbrCurrencyDto> cbrCurrencyDtos;

    @XmlElement(name = "Date")
    private Instant date;
}
