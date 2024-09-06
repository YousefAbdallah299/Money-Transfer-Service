package com.transfer.dto;

import jakarta.persistence.Column;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ReturnFavoritesDTO {
    private String recipientName;

    @Column(nullable = false)
    private Long recipientAccountId;
}
