package com.transfer.dto.request;


import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditCustomerDTO {
    private String fullName;
    @Email
    private String email;
    private String country;
    private LocalDate dateOfBirth;
    private String phoneNumber;
}
