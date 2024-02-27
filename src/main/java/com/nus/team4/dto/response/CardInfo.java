package com.nus.team4.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardInfo {
    private String iban;
    private String cvc;

    public CardInfo(String iban, String cvc) {
        this.iban = iban;
        this.cvc = cvc;
    }
}
