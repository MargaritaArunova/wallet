package com.wallet.model.dto.mail;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Mail {

    /**
     * Email получателя письма
     */
    private String recipient;

    /**
     * Тема письма
     */
    private String subject;

    /**
     * Содержимое письма
     */
    private String message;
}
