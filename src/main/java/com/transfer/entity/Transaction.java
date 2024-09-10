package com.transfer.entity;

import com.transfer.dto.response.TransactionResponseDTO;
import com.transfer.dto.enums.AccountCurrency;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Account account;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountCurrency currency;

    @Column(nullable = false)
    private String recieverAccountNumber;

    private String recieverAccountName;

    @Column(nullable = false)
    private Double amountTransferred;

    @CreationTimestamp
    private LocalDateTime createdAt;



    public TransactionResponseDTO toDTO() {
        return TransactionResponseDTO.builder()
                .amountTransferred(this.amountTransferred)
                .recieverAccountNumber(this.recieverAccountNumber)
                .id(this.id)
                .createdAt(this.createdAt)
                .senderName(this.account.getAccountName())
                .senderAccountNumber(this.account.getAccountNumber())
                .currency(this.currency)
                .recieverAccountName(this.recieverAccountName)
                .build();
    }

}
