package com.test.customercrud.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomerCreateRequestDTO {
    @NotBlank
    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
    private String fullName;

    @NotBlank
    @Email(message = "Email should be valid")
    @Size(min = 2, max = 100, message = "Email must be between 2 and 100 characters")
    private String email;

    @Pattern(regexp = "\\+\\d{6,14}", message = "Phone number must start with '+' and contain 6 to 14 digits")
    private String phone;
}
