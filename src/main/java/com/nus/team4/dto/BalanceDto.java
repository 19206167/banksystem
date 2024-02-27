package com.nus.team4.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class BalanceDto {

    @NotBlank(message = "iban is required")
    String iban;

    @NotBlank(message = "amount is required")
    BigDecimal amount;
}
