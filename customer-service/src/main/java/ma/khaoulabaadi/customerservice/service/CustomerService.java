package ma.khaoulabaadi.customerservice.service;

import java.util.List;

import ma.khaoulabaadi.customerservice.entities.Customer;
import ma.khaoulabaadi.customerservice.repository.CustomerRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Tool(description = "Get all customers")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Tool(description = "Find a customer by id")
    public Customer findCustomerById(@ToolParam(description = "The customer id") Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found!"));
    }

    @Tool(description = "Save a new customer")
    public Customer saveCustomer(
            @ToolParam(description = "Customer name") String name,
            @ToolParam(description = "Customer email") String email) {
        Customer customer = Customer.builder().name(name).email(email).build();
        return customerRepository.save(customer);
    }
}