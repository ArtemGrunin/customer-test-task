package com.test.customercrud.service.Impl;

import com.test.customercrud.dto.CustomerCreateRequestDTO;
import com.test.customercrud.dto.CustomerResponseDTO;
import com.test.customercrud.dto.CustomerUpdateRequestDTO;
import com.test.customercrud.exception.CustomerNotFoundException;
import com.test.customercrud.mapper.CustomerMapperImpl;
import com.test.customercrud.model.Customer;
import com.test.customercrud.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static com.test.customercrud.service.Impl.CustomerServiceImplTest.TestResources.CUSTOMER_ID;
import static com.test.customercrud.service.Impl.CustomerServiceImplTest.TestResources.buildCustomer;
import static com.test.customercrud.service.Impl.CustomerServiceImplTest.TestResources.buildCustomerCreateRequestDTO;
import static com.test.customercrud.service.Impl.CustomerServiceImplTest.TestResources.buildCustomerResponseDTO;
import static com.test.customercrud.service.Impl.CustomerServiceImplTest.TestResources.buildCustomerUpdateRequestDTO;
import static com.test.customercrud.service.Impl.CustomerServiceImplTest.TestResources.buildSavedCustomer;
import static com.test.customercrud.service.Impl.CustomerServiceImplTest.TestResources.buildUpdatedCustomer;
import static com.test.customercrud.service.Impl.CustomerServiceImplTest.TestResources.buildUpdatedCustomerResponseDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private CustomerMapperImpl customerMapper;
    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void givenCreateCustomerRequest_whenCreate_thenCustomerCreated() {
        when(customerRepository.save(buildCustomer())).thenReturn(buildSavedCustomer());
        CustomerResponseDTO responseDTO = customerService.create(buildCustomerCreateRequestDTO());

        assertThat(responseDTO).isEqualTo(buildCustomerResponseDTO());
    }

    @Test
    void givenCustomersExist_whenGetAll_thenAllActiveCustomersReturned() {
        when(customerRepository.findAll()).thenReturn(List.of(buildSavedCustomer()));
        List<CustomerResponseDTO> responseDTOs = customerService.getAll();

        assertThat(responseDTOs).containsExactly(buildCustomerResponseDTO());
    }

    @Test
    void givenCustomerId_whenGet_thenCustomerReturned() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(buildSavedCustomer()));
        CustomerResponseDTO responseDTO = customerService.get(CUSTOMER_ID);

        assertThat(responseDTO).isEqualTo(buildCustomerResponseDTO());
    }

    @Test
    void givenInvalidCustomerId_whenGet_thenCustomerNotFoundExceptionThrown() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.get(CUSTOMER_ID))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("Customer not found with id: %s".formatted(CUSTOMER_ID));
    }

    @Test
    void givenUpdateCustomerRequest_whenUpdate_thenCustomerUpdated() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(buildSavedCustomer()));
        when(customerRepository.save(buildUpdatedCustomer())).thenReturn(buildUpdatedCustomer());
        CustomerResponseDTO responseDTO = customerService.update(CUSTOMER_ID, buildCustomerUpdateRequestDTO());

        assertThat(responseDTO).isEqualTo(buildUpdatedCustomerResponseDTO());
    }

    @Test
    void givenInvalidCustomerId_whenUpdate_thenCustomerNotFoundExceptionThrown() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.update(CUSTOMER_ID, buildCustomerUpdateRequestDTO()))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("Customer not found with id: %s".formatted(CUSTOMER_ID));
    }

    @Test
    void givenCustomerId_whenDelete_thenCustomerDeactivated() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(buildSavedCustomer()));
        customerService.delete(CUSTOMER_ID);
        verify(customerRepository).deleteById(CUSTOMER_ID);
    }

    @Test
    void givenInvalidCustomerId_whenDelete_thenCustomerNotFoundExceptionThrown() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.delete(CUSTOMER_ID))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("Customer not found with id: %s".formatted(CUSTOMER_ID));
    }

    static class TestResources {
        static final Long CUSTOMER_ID = 1L;
        static final String FULL_NAME = "John Doe";
        static final String EMAIL = "john.doe@example.com";
        static final String PHONE = "+380123321123";
        static final String NEW_PHONE = "+380987654321";
        public static final String NEW_NAME = "Jane Doe";

        public static CustomerCreateRequestDTO buildCustomerCreateRequestDTO() {
            return CustomerCreateRequestDTO.builder()
                    .fullName(FULL_NAME)
                    .email(EMAIL)
                    .phone(PHONE)
                    .build();
        }

        public static CustomerResponseDTO buildCustomerResponseDTO() {
            return CustomerResponseDTO.builder()
                    .id(CUSTOMER_ID)
                    .fullName(FULL_NAME)
                    .email(EMAIL)
                    .phone(PHONE)
                    .build();
        }

        public static CustomerUpdateRequestDTO buildCustomerUpdateRequestDTO() {
            return CustomerUpdateRequestDTO.builder()
                    .id(CUSTOMER_ID)
                    .fullName(NEW_NAME)
                    .email(EMAIL)
                    .phone(NEW_PHONE)
                    .build();
        }

        public static Customer buildCustomer() {
            return Customer.builder()
                    .fullName(FULL_NAME)
                    .email(EMAIL)
                    .phone(PHONE)
                    .build();
        }

        public static Customer buildSavedCustomer() {
            return Customer.builder()
                    .id(CUSTOMER_ID)
                    .fullName(FULL_NAME)
                    .email(EMAIL)
                    .phone(PHONE)
                    .isActive(true)
                    .build();
        }

        public static Customer buildDeletedCustomer() {
            return Customer.builder()
                    .id(CUSTOMER_ID)
                    .fullName(FULL_NAME)
                    .email(EMAIL)
                    .phone(PHONE)
                    .isActive(false)
                    .build();
        }

        public static Customer buildUpdatedCustomer() {
            return Customer.builder()
                    .id(CUSTOMER_ID)
                    .fullName(NEW_NAME)
                    .email(EMAIL)
                    .phone(NEW_PHONE)
                    .isActive(true)
                    .build();
        }

        public static CustomerResponseDTO buildUpdatedCustomerResponseDTO() {
            return CustomerResponseDTO.builder()
                    .id(CUSTOMER_ID)
                    .fullName(NEW_NAME)
                    .email(EMAIL)
                    .phone(NEW_PHONE)
                    .build();
        }
    }
}
