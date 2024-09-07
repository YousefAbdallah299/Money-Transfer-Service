package com.transfer.dto.request;

import com.transfer.dto.enums.AccountCurrency;
import com.transfer.dto.enums.AccountType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter

public class CreateAccountRequestDTO {
    @Enumerated(EnumType.STRING)

    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    private AccountCurrency currency;

    @NotBlank
    private String accountName;

    @NotBlank
    private String accountDescription;


}
