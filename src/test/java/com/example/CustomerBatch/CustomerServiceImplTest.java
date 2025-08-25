package com.example.CustomerBatch.services;

import com.example.CustomerBatch.dto.CustomerDTO;
import com.example.CustomerBatch.entities.Customer;
import com.example.CustomerBatch.mappers.CustomerMapper;
import com.example.CustomerBatch.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveCustomer_shouldSave_whenEmailValidAndNotExists() {
        CustomerDTO dto = new CustomerDTO();
        dto.setEmail("test@example.com");

        Customer entity = new Customer();
        entity.setEmail("test@example.com");

        when(customerRepository.verifyIfEmailExists(dto.getEmail())).thenReturn(false);
        when(customerMapper.from(dto)).thenReturn(entity);
        when(customerRepository.save(entity)).thenReturn(entity);
        when(customerMapper.from(entity)).thenReturn(dto);

        CustomerDTO result = customerService.saveCustomer(dto);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());

        verify(customerRepository, times(1)).save(entity);
    }

    @Test
    void saveCustomer_shouldThrowException_whenEmailNull() {
        CustomerDTO dto = new CustomerDTO();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> customerService.saveCustomer(dto));
        assertEquals("Email is required", exception.getMessage());
    }

    @Test
    void saveCustomer_shouldThrowException_whenEmailExists() {
        CustomerDTO dto = new CustomerDTO();
        dto.setEmail("exists@example.com");

        when(customerRepository.verifyIfEmailExists(dto.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> customerService.saveCustomer(dto));
        assertEquals("This email already exists : exists@example.com", exception.getMessage());
    }

    @Test
    void getAllCustomers_shouldReturnList() {
        Customer customer1 = new Customer();
        Customer customer2 = new Customer();

        CustomerDTO dto1 = new CustomerDTO();
        CustomerDTO dto2 = new CustomerDTO();

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));
        when(customerMapper.from(customer1)).thenReturn(dto1);
        when(customerMapper.from(customer2)).thenReturn(dto2);

        List<CustomerDTO> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void deleteCustomer_shouldDelete_whenCustomerExists() {
        Customer customer = new Customer();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1L);

        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCustomer_shouldThrowException_whenCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> customerService.deleteCustomer(1L));
        assertEquals("Customer not found", exception.getMessage());
    }
}
