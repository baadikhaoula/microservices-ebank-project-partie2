package ma.khaoulabaadi.customerservice;

import ma.khaoulabaadi.customerservice.service.CustomerService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    // Expose les méthodes @Tool de CustomerService au serveur MCP
    @Bean
    public ToolCallbackProvider customerTools(CustomerService customerService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(customerService)
                .build();
    }

    @Bean
    CommandLineRunner start(CustomerService customerService) {
        return args -> List.of("Mohamed", "Imane", "Yassine").forEach(name ->
                customerService.saveCustomer(name, name.toLowerCase() + "@gmail.com"));
    }
}