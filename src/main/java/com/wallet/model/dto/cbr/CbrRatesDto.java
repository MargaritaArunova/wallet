package com.wallet.model.dto.cbr;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@Accessors(chain = true)
@XmlRootElement(name = "ValCurs")
@XmlAccessorType(XmlAccessType.FIELD)
public class CbrRatesDto implements Serializable {

    @XmlElement(name = "Valute")
    private List<CbrCurrencyDto> cbrCurrencyDtos;

    @XmlElement(name = "Date")
    private Instant date;
}
