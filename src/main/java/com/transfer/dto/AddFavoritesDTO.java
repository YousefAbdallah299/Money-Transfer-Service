package com.transfer.dto;

import jakarta.persistence.Column;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AddFavoritesDTO {
    @Column(nullable = false)
    private Long recipientAccountId;
}
