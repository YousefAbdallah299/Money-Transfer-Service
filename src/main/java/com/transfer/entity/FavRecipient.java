package com.transfer.entity;

import com.transfer.dto.response.FavoritesResponseDTO;
import com.transfer.entity.key.FavRecipientId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavRecipient {

    @EmbeddedId
    private FavRecipientId id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account recipientAccount;

    @Column(nullable = false)
    private String recipientName;



    public FavoritesResponseDTO toDTO(){
        return FavoritesResponseDTO.builder()
                .recipientAccountId(this.id.getRecipientAccountID())
                .recipientName(this.recipientName)
                .build();
    }




}
