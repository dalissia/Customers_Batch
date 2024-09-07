package com.example.CustomerBatch.services;


import com.example.CustomerBatch.dto.CustomerDTO;
import com.example.CustomerBatch.mappers.CustomerMapper;
import com.example.CustomerBatch.entities.Customer;
import com.example.CustomerBatch.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
   @Autowired
   CustomerRepository customerRepository;
   @Autowired
   CustomerMapper customerMapper;



    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        if (customerDTO.getEmail()==null)
            throw  new RuntimeException("Email is required");
        Boolean emailExists = customerRepository.verifyIfEmailExists(customerDTO.getEmail());
        if(emailExists) throw new RuntimeException(String.format("This email already exists : %s",customerDTO.getEmail()));
        Customer customer = customerRepository.save(customerMapper.from(customerDTO));
        return customerMapper.from(customer);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::from).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
         Customer customer = customerRepository.findById(id).orElse(null);
         if(customer==null)
             throw  new RuntimeException("Customer not found");

        return null;
    }

    @Override
    public void deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new RuntimeException("Customer not found");
        customerRepository.deleteById(customerId);
    }
}
