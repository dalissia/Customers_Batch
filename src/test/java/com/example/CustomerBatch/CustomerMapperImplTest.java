package com.example.CustomerBatch.mappers;

import com.example.CustomerBatch.dto.CustomerDTO;
import com.example.CustomerBatch.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperImplTest {

    private CustomerMapperImpl customerMapper;

    @BeforeEach
    void setUp() {
        customerMapper = new CustomerMapperImpl();
    }

    @Test
    void testDtoToEntityMapping() throws Exception {
        CustomerDTO dto = new CustomerDTO();
        dto.setContactNo("1234567890");
        dto.setDob("25-08-2025"); // format dd-MM-yyyy

        Customer entity = customerMapper.from(dto);

        assertNotNull(entity);
        assertEquals("1234567890", entity.getPhoneNumber());

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sdf.format(entity.getDob());
        assertEquals("25-08-2025", formattedDate);
    }

    @Test
    void testEntityToDtoMapping() {
        Customer entity = new Customer();
        entity.setPhoneNumber("9876543210");

        // créer une date spécifique
        Date date = new Date(2025-1900, 7, 25); // année = année-1900, mois = 0-index
        entity.setDob(date);

        CustomerDTO dto = customerMapper.from(entity);

        assertNotNull(dto);
        assertEquals("9876543210", dto.getContactNo());
        assertEquals("24-08-2025", dto.getDob());
    }
}
