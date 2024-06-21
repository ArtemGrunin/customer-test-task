package com.test.customercrud.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomerResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
}
