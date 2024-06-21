package com.test.customercrud.mapper;

import com.test.customercrud.dto.CustomerCreateRequestDTO;
import com.test.customercrud.dto.CustomerResponseDTO;
import com.test.customercrud.dto.CustomerUpdateRequestDTO;
import com.test.customercrud.model.Customer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    Customer toCustomer(CustomerCreateRequestDTO dto);

    CustomerResponseDTO toCustomerResponseDTO(Customer customer);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomerFromDTO(CustomerUpdateRequestDTO dto, @MappingTarget Customer customer);
}
