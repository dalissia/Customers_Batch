package com.example.CustomerBatch.web;

import com.example.CustomerBatch.dto.CustomerDTO;
import com.example.CustomerBatch.entities.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.CustomerBatch.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class CustomerController {
   @Autowired
    CustomerService customerService ;
//GetAllCustomers
   @GetMapping("/customers")
    public List<CustomerDTO> customerList(){
       return customerService.getAllCustomers();
   }

   @GetMapping("/customers/{id}")
    public ResponseEntity <?> customerById(@PathVariable Long id){
       try {
           CustomerDTO customerById = customerService.getCustomerById(id);
           return ResponseEntity.ok().body(customerById);
       } catch (Exception e){
           return ResponseEntity.internalServerError().body(e.getMessage());
       }
   }

   @PostMapping("costumers")
   public ResponseEntity <CustomerDTO> newCustomer(@RequestBody CustomerDTO customerDTO){
       CustomerDTO savedCustomer  =  customerService.saveCustomer(customerDTO);
       return  ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
   }

   @DeleteMapping("/customers/{customerId}")
    public ResponseEntity deleteCustomer (@PathVariable Long  customerId){
       try{
           customerService.deleteCustomer(customerId);
           return ResponseEntity.noContent().build();
       } catch (RuntimeException e) {
           return ResponseEntity.internalServerError().body(e.getMessage());
       }

   }




}
