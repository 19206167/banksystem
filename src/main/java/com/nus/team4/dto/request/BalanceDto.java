package com.nus.team4.dto.request;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class BalanceDto {

    @NotBlank(message = "iban is required")
    String iban;

    @NotBlank(message = "amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    BigDecimal amount;
}
