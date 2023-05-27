package com.wallet.model.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationMail {

    CREATE(
        "Создана новая учётная запись в Wallet",
        """
            Здравствуйте!
            На эту почту создана учётная запись в приложении Wallet.
            """
    );

    private final String subject;
    private final String message;

}
