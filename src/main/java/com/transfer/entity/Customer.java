package com.transfer.entity;

import com.transfer.dto.response.RegisterCustomerResponseDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "customers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;  // Make sure to hash passwords before saving

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private Set<FavRecipient> favoriteRecipients = new HashSet<>();

    // Convert Customer entity to RegisterCustomerResponseDTO
    public RegisterCustomerResponseDTO toResponse() {
        return RegisterCustomerResponseDTO.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}