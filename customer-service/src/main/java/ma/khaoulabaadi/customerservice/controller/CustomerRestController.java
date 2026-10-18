package ma.khaoulabaadi.customerservice.controller;

import ma.khaoulabaadi.customerservice.entities.Customer;
import ma.khaoulabaadi.customerservice.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerRestController {
    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public List<Customer> customerList() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/customers/{id}")
    public Customer customerById(@PathVariable Long id) {
        return customerService.findCustomerById(id);
    }

    // ✅ Méthode corrigée
    @PostMapping("/customers")
    public Customer saveCustomer(@RequestBody Customer customer) {
        // Extraire les données de l'objet Customer
        return customerService.saveCustomer(
                customer.getName(),
                customer.getEmail()
        );
    }
}