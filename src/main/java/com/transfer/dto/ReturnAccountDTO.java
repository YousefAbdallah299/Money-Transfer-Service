package com.transfer.dto;


import com.transfer.dto.enums.AccountCurrency;
import com.transfer.dto.enums.AccountType;
import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ReturnAccountDTO {


    private Long id;

    private String accountNumber;

    private AccountType accountType;

    private Double balance;

    private AccountCurrency currency;

    private String accountName;

    private String accountDescription;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
