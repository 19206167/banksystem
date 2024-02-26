package com.nus.team4.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
public class AccountOpenForm {

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid 3-letter ISO code")
    private String currency;

    @NotBlank(message = "Account Type is required")
    private String accountType;

    @NotBlank(message = "Status is required")
    private String status;

    @NotBlank(message = "Security Code is required")
    @Size(min = 3, max = 3, message = "Security Code must be 3 digits")
    private String securityCode;

    private BigDecimal balance = BigDecimal.ZERO;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = ".+@.+\\..+", message = "Invalid email format")
    private String email;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone number is required")
    private String phone;

}
