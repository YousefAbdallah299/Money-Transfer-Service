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
    private Long recieverID;

    @Column(nullable = false)
    private Double amountTransferred;

    @CreationTimestamp
    private LocalDateTime createdAt;



    public TransactionResponseDTO toDTO() {
        return TransactionResponseDTO.builder()
                .amountTransferred(this.amountTransferred)
                .recieverID(this.recieverID)
                .id(this.id)
                .createdAt(this.createdAt)
                .senderID(this.account.getId())
                .currency(this.currency)
                .build();
    }

}
