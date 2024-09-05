package com.transfer.dto;

import com.transfer.dto.enums.AccountCurrency;
import com.transfer.dto.enums.AccountType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class RegisterCustomerRequest {

    @NotBlank
    private String name;

    @Email
    private String email;

    @Size(min = 8)
    private String password;

    @Enumerated(EnumType.STRING)
    private AccountCurrency accountCurrency;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;


}
