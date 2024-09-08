package com.transfer.entity;

import com.transfer.dto.response.FavoritesResponseDTO;
import com.transfer.entity.key.FavRecipientId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorite_recipients")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavRecipient {

    @EmbeddedId
    private FavRecipientId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("customerID")  // Refers to the customerID in FavRecipientId
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipientAccountID")  // Refers to the recipientAccountID in FavRecipientId
    private Account recipientAccount;

    @Column(nullable = false)
    private String recipientName;

    // Convert FavRecipient entity to FavoritesResponseDTO
    public FavoritesResponseDTO toDTO() {
        return FavoritesResponseDTO.builder()
                .recipientAccountId(this.id.getRecipientAccountID())
                .recipientName(this.recipientName)
                .build();
    }
}