package com.test.customercrud.controller;

import com.test.customercrud.dto.CustomerCreateRequestDTO;
import com.test.customercrud.dto.CustomerResponseDTO;
import com.test.customercrud.dto.CustomerUpdateRequestDTO;
import com.test.customercrud.exception.CustomerNotFoundException;
import com.test.customercrud.mapper.CustomerMapperImpl;
import com.test.customercrud.service.CustomerService;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.TimeZone;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static com.test.customercrud.controller.CustomerControllerTest.TestResources.CUSTOMER_ID;
import static com.test.customercrud.controller.CustomerControllerTest.TestResources.INVALID_CUSTOMER_ID;
import static com.test.customercrud.controller.CustomerControllerTest.TestResources.NEW_PHONE;
import static com.test.customercrud.controller.CustomerControllerTest.TestResources.PHONE;
import static com.test.customercrud.controller.CustomerControllerTest.TestResources.buildCustomerCreateRequestDTO;
import static com.test.customercrud.controller.CustomerControllerTest.TestResources.buildCustomerResponseDTO;
import static com.test.customercrud.controller.CustomerControllerTest.TestResources.buildCustomerUpdateRequestDTO;
import static com.test.customercrud.controller.CustomerControllerTest.TestResources.buildUpdatedCustomerResponseDTO;
import static com.test.customercrud.controller.CustomerControllerTest.TestResources.getFileContent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@ExtendWith(SpringExtension.class)
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private CustomerMapperImpl mapper;
    @MockBean
    private CustomerService customerService;
    @BeforeEach
    void beforeEach() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    void givenValidCustomerDTO_whenCreateCustomer_shouldReturnCreatedCustomer() throws Exception {
        when(customerService.create(buildCustomerCreateRequestDTO(PHONE)))
                .thenReturn(buildCustomerResponseDTO(PHONE));

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getFileContent("customer/create-request.json")))
                .andExpect(status().isCreated())
                .andExpect(content().json(getFileContent("customer/created-response.json")));

        verify(customerService).create(buildCustomerCreateRequestDTO(PHONE));
    }

    @Test
    void givenDuplicateEmail_whenCreateCustomer_shouldReturnInternalServerError() throws Exception {
        doThrow(new RuntimeException()).when(customerService).create(buildCustomerCreateRequestDTO(PHONE));

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getFileContent("customer/create-request.json")))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"message\":\"Internal server error\"}"));

        verify(customerService).create(buildCustomerCreateRequestDTO(PHONE));
    }

    @Test
    void givenShortFullName_whenCreateCustomer_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getFileContent("customer/invalid-short-fullname-request.json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenLongFullName_whenCreateCustomer_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getFileContent("customer/invalid-long-fullname-request.json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidPhone_whenCreateCustomer_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getFileContent("customer/invalid-phone-request.json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenMissingPhone_whenCreateCustomer_shouldReturnCreatedCustomer() throws Exception {
        when(customerService.create(buildCustomerCreateRequestDTO(null)))
                .thenReturn(buildCustomerResponseDTO(null));

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getFileContent("customer/missing-phone-request.json")))
                .andExpect(status().isCreated())
                .andExpect(content().json(getFileContent("customer/created-response-missing-phone.json")));

        verify(customerService).create(buildCustomerCreateRequestDTO(null));
    }

    @Test
    void givenCustomersExist_whenGetAllCustomers_shouldReturnCustomerList() throws Exception {
        when(customerService.getAll()).thenReturn(List.of(buildCustomerResponseDTO(PHONE)));

        mockMvc.perform(get("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getFileContent("customer/all-customers-response.json")));

        verify(customerService).getAll();
    }

    @Test
    void givenValidCustomerId_whenGetCustomerById_shouldReturnCustomer() throws Exception {
        when(customerService.get(CUSTOMER_ID)).thenReturn(buildCustomerResponseDTO(PHONE));

        mockMvc.perform(get("/api/customers/" + CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getFileContent("customer/created-response.json")));

        verify(customerService).get(CUSTOMER_ID);
    }

    @Test
    void givenInvalidCustomerId_whenGetCustomerById_shouldReturnNotFound() throws Exception {
        when(customerService.get(INVALID_CUSTOMER_ID))
                .thenThrow(new CustomerNotFoundException(
                        "Customer not found with id: %s".formatted(INVALID_CUSTOMER_ID)));

        MvcResult result = mockMvc.perform(get("/api/customers/" + INVALID_CUSTOMER_ID))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResolvedException())
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("Customer not found with id: %s".formatted(INVALID_CUSTOMER_ID));

        verify(customerService).get(INVALID_CUSTOMER_ID);
    }

    @Test
    void givenValidCustomerId_whenUpdateCustomer_shouldReturnUpdatedCustomer() throws Exception {
        when(customerService.update(CUSTOMER_ID, buildCustomerUpdateRequestDTO(NEW_PHONE)))
                .thenReturn(buildUpdatedCustomerResponseDTO(NEW_PHONE));

        mockMvc.perform(put("/api/customers/" + CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getFileContent("customer/update-request.json")))
                .andExpect(status().isOk())
                .andExpect(content().json(getFileContent("customer/updated-response.json")));

        verify(customerService).update(CUSTOMER_ID, buildCustomerUpdateRequestDTO(NEW_PHONE));
    }

    @Test
    void givenValidCustomerId_whenDeleteCustomer_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/customers/{id}", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).delete(CUSTOMER_ID);
    }

    @Test
    void givenInvalidCustomerId_whenDeleteCustomer_shouldReturnNotFound() throws Exception {
        doThrow(new CustomerNotFoundException(
                "Customer not found with id: %s".formatted(INVALID_CUSTOMER_ID)))
                .when(customerService).delete(INVALID_CUSTOMER_ID);

        MvcResult result = mockMvc.perform(delete("/api/customers/{id}", INVALID_CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResolvedException())
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("Customer not found with id: %s".formatted(INVALID_CUSTOMER_ID));

        verify(customerService).delete(INVALID_CUSTOMER_ID);
    }

    static class TestResources {
        static final Long CUSTOMER_ID = 1L;
        static final Long INVALID_CUSTOMER_ID = 99L;
        static final String FULL_NAME = "John Doe";
        static final String EMAIL = "john.doe@example.com";
        static final String PHONE = "+380123321123";
        static final String NEW_PHONE = "+380987654321";
        public static final String NEW_NAME = "Jane Doe";

        static CustomerCreateRequestDTO buildCustomerCreateRequestDTO(String phone) {
            return CustomerCreateRequestDTO.builder()
                    .fullName(FULL_NAME)
                    .email(EMAIL)
                    .phone(phone)
                    .build();
        }

        static CustomerResponseDTO buildCustomerResponseDTO(String phone) {
            return CustomerResponseDTO.builder()
                    .id(CUSTOMER_ID)
                    .fullName(FULL_NAME)
                    .email(EMAIL)
                    .phone(phone)
                    .build();
        }

        static CustomerResponseDTO buildUpdatedCustomerResponseDTO(String phone) {
            return CustomerResponseDTO.builder()
                    .id(CUSTOMER_ID)
                    .fullName(NEW_NAME)
                    .email(EMAIL)
                    .phone(phone)
                    .build();
        }

        static CustomerUpdateRequestDTO buildCustomerUpdateRequestDTO(String phone) {
            return CustomerUpdateRequestDTO.builder()
                    .id(CUSTOMER_ID)
                    .fullName(NEW_NAME)
                    .email(EMAIL)
                    .phone(phone)
                    .build();
        }

        @SneakyThrows
        static String getFileContent(String path) {
            File resource = new ClassPathResource(path).getFile();
            return new String(Files.readAllBytes(resource.toPath()));
        }
    }
}
