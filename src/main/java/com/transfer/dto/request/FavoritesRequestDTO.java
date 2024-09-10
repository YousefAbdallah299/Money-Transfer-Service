package com.transfer.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FavoritesRequestDTO {

    @Column(nullable = false)
    @NotBlank
    private String recipientAccountName;

    @Column(nullable = false)
    @NotBlank
    private String recipientAccountNumber;
}
