package com.test.customercrud.service.Impl;

import com.test.customercrud.dto.CustomerCreateRequestDTO;
import com.test.customercrud.dto.CustomerResponseDTO;
import com.test.customercrud.dto.CustomerUpdateRequestDTO;
import com.test.customercrud.exception.CustomerNotFoundException;
import com.test.customercrud.mapper.CustomerMapper;
import com.test.customercrud.model.Customer;
import com.test.customercrud.repository.CustomerRepository;
import com.test.customercrud.service.CustomerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerResponseDTO create(CustomerCreateRequestDTO dto) {
        Customer customer = customerMapper.toCustomer(dto);
        customer = customerRepository.save(customer);
        return customerMapper.toCustomerResponseDTO(customer);
    }

    @Override
    public List<CustomerResponseDTO> getAll() {
        return customerRepository.findAll().stream()
                .filter(Customer::getIsActive)
                .map(customerMapper::toCustomerResponseDTO)
                .toList();
    }

    @Override
    public CustomerResponseDTO get(Long id) {
        Customer customer = customerRepository.findById(id)
                .filter(Customer::getIsActive)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: %s".formatted(id)));
        return customerMapper.toCustomerResponseDTO(customer);
    }

    @Override
    public CustomerResponseDTO update(Long id, CustomerUpdateRequestDTO dto) {
        Customer customer = customerRepository.findById(id)
                .filter(Customer::getIsActive)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: %s".formatted(id)));
        customerMapper.updateCustomerFromDTO(dto, customer);
        customer = customerRepository.save(customer);
        return customerMapper.toCustomerResponseDTO(customer);
    }

    @Override
    public void delete(Long id) {
        Customer customer = customerRepository.findById(id)
                .filter(Customer::getIsActive)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: %s".formatted(id)));
        customer.setIsActive(false);
        customerRepository.save(customer);
    }
}
