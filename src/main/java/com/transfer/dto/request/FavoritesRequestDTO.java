package com.transfer.dto.request;

import jakarta.persistence.Column;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FavoritesRequestDTO {
    @Column(nullable = false)
    private Long recipientAccountId;
}
