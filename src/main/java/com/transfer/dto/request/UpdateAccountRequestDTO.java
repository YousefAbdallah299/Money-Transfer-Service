package com.transfer.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UpdateAccountRequestDTO {
    @NotNull
    private Long accountId;

    @NotBlank
    private String accountName;

    @NotBlank
    private String accountDescription;
}
