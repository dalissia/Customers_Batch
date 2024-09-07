package com.example.CustomerBatch.mappers;

import com.example.CustomerBatch.dto.CustomerDTO;
import com.example.CustomerBatch.entities.Customer;

public interface CustomerMapper {
    Customer from(CustomerDTO customerDTO);
    CustomerDTO from(Customer customer);
}
