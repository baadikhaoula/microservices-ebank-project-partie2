package ma.khaoulabaadi.customerservice.service;

import java.util.List;

import ma.khaoulabaadi.customerservice.entities.Customer;
import ma.khaoulabaadi.customerservice.repository.CustomerRepository;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Méthode pour récupérer tous les clients
    @McpTool(description = "Get all customers")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Méthode pour trouver un client par son ID
    @McpTool(description = "Find a customer by id")
    public Customer findCustomerById(@McpToolParam(description = "The customer id") Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found!"));
    }

    // Méthode pour sauvegarder un client (créer ou mettre à jour)
    @McpTool(description = "Save a new customer")
    public Customer saveCustomer(
            @McpToolParam(description = "Customer name") String name,
            @McpToolParam(description = "Customer email") String email) {
        Customer customer = Customer.builder()
                .name(name)
                .email(email)
                .build();

        return customerRepository.save(customer);
    }
}