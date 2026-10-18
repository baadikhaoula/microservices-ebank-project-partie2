package ma.khaoulabaadi.ebankservice.services;

import ma.khaoulabaadi.ebankservice.entities.BankAccount;
import ma.khaoulabaadi.ebankservice.feign.CustomerRestClient;
import ma.khaoulabaadi.ebankservice.model.Customer;
import ma.khaoulabaadi.ebankservice.repository.BankAccountRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EbankService {

    private final BankAccountRepository bankAccountRepository;
    private final CustomerRestClient customerRestClient;

    public EbankService(BankAccountRepository bankAccountRepository, CustomerRestClient customerRestClient) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerRestClient = customerRestClient;
    }

    @Tool(description = "Get all bank accounts")
    public List<BankAccount> getAllBankAccount() {
        return bankAccountRepository.findAll();
    }

    @Tool(description = "Find a bank account by its ID")
    public BankAccount getBankAccountById(@ToolParam(description = "The bank account ID") String id) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank Account Not Found"));
        bankAccount.setCustomer(customerRestClient.getCustomerById(bankAccount.getCustomerId()));
        return bankAccount;
    }

    @Tool(description = "Save a new bank account")
    public BankAccount save(@ToolParam(description = "The bank account to save") BankAccount bankAccount) {
        try {
            Customer customer = customerRestClient.getCustomerById(bankAccount.getCustomerId());
            bankAccount.setId(UUID.randomUUID().toString());
            bankAccount.setCreatedAt(new Date());
            return bankAccountRepository.save(bankAccount);
        } catch (Exception e) {
            throw new RuntimeException("Error saving bank account: " + e.getMessage());
        }
    }
}