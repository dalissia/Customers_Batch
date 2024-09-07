package com.example.CustomerBatch.services;

import com.example.CustomerBatch.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO getCustomerById(Long id );
    void deleteCustomer(Long customerId);



}
