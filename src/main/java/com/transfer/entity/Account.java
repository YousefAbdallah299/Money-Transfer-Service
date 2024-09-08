package com.transfer.entity;


import com.transfer.dto.response.AccountResponseDTO;
import com.transfer.dto.enums.AccountCurrency;
import com.transfer.dto.enums.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "accounts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(nullable = false)
    private Double balance;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountCurrency currency;

    private String accountName;
    private String accountDescription;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Transaction> transactions = new HashSet<>();

    // Convert Account entity to AccountResponseDTO
    public AccountResponseDTO toDTO() {
        return AccountResponseDTO.builder()
                .id(this.id)
                .accountNumber(this.accountNumber)
                .accountType(this.accountType)
                .balance(this.balance)
                .currency(this.currency)
                .accountName(this.accountName)
                .accountDescription(this.accountDescription)
                .active(this.active)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}