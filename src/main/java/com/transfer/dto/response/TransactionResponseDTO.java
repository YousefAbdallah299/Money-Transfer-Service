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

    private Long senderID;


    private AccountCurrency currency;

    private Long recieverID;

    private Double amountTransferred;

    private LocalDateTime createdAt;

}
