package com.test.customercrud.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomerUpdateRequestDTO {
    @NotNull
    private Long id;
    @NotBlank
    @Size(min = 2, max = 50)
    private String fullName;
    private String email;
    @Pattern(regexp = "^\\+\\d{6,14}$",
            message = "Phone number must start with '+'")
    private String phone;
}
