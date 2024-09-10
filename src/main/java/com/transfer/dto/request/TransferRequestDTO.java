package com.transfer.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class TransferRequestDTO {
    @NotBlank
    private String senderAccountNumber;
    @NotBlank
    private String recipientAccountNumber;
    @NotBlank
    private String RecipientName;
    @NotNull
    private Double Amount;
}
