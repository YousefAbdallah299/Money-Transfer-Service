package com.transfer.dto.response;

import jakarta.persistence.Column;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FavoritesResponseDTO {
    private String recipientName;

    @Column(nullable = false)
    private Long recipientAccountId;
}
