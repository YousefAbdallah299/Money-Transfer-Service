package com.transfer.dto.response;

import com.transfer.dto.enums.AccountCurrency;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TransactionResponseDTO {
    private Long id;

    private String senderName;

    private String senderAccountNumber;

    private AccountCurrency currency;

    private String recieverAccountNumber;

    private String recieverAccountName;

    private Double amountTransferred;

    private LocalDateTime createdAt;

}
