package com.transfer.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UpdateAccountDTO {
    @NotNull
    private Long accountId;

    @NotBlank
    private String accountName;

    @NotBlank
    private String accountDescription;
}
