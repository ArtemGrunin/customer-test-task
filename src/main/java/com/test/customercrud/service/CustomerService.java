package com.test.customercrud.service;

import com.test.customercrud.dto.CustomerCreateRequestDTO;
import com.test.customercrud.dto.CustomerResponseDTO;
import com.test.customercrud.dto.CustomerUpdateRequestDTO;
import java.util.List;

public interface CustomerService {
    CustomerResponseDTO create(CustomerCreateRequestDTO request);

    CustomerResponseDTO get(Long id);

    List<CustomerResponseDTO> getAll();

    CustomerResponseDTO update(Long id, CustomerUpdateRequestDTO request);

    void delete(Long id);
}
