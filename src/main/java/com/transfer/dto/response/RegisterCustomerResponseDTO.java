package com.transfer.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class RegisterCustomerResponseDTO {

    private Long id;

    private String name;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
