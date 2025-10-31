package com.example.demo.BootStrapData;

import com.example.demo.dao.*;
import com.example.demo.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class BootStrapData implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ExcursionRepository excursionRepository;

    @Autowired
    private DivisionRepository divisionRepository;

    public BootStrapData(CustomerRepository customerRepository, CartItemRepository cartItemRepository, ExcursionRepository excursionRepository, DivisionRepository divisionRepository) {
        this.customerRepository = customerRepository;
        this.cartItemRepository = cartItemRepository;
        this.excursionRepository = excursionRepository;
        this.divisionRepository = divisionRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Set<Customer> customersToAdd = new HashSet<>(customerRepository.findAll());
        List<String> firstNames = List.of("Liam", "Olivia", "Noah", "Ava", "Ethan", "Sophia");
        List<String> lastNames = List.of("Anderson", "Martinez", "Nguyen", "Brown", "Thompson", "Lee");
        List<String> addresses = List.of(
                "101 Lakeview Drive",
                "22 Pine Street",
                "45 Oak Avenue",
                "89 River Road",
                "200 Elm Blvd",
                "333 Cedar Lane"
        );
        List<String> phones = List.of(
                "(555)555-1111",
                "(555)555-2222",
                "(555)555-3333",
                "(555)555-4444",
                "(555)555-5555",
                "(555)555-6666"
        );
        List<String> postalCodes = List.of("48209", "60611", "30301", "85001", "97201", "27513");

        while (customersToAdd.size() < 6) {
            int index = customersToAdd.size();
            Customer customer = new Customer();
            customer.setFirstName(firstNames.get(index));
            customer.setLastName(lastNames.get(index));
            customer.setPhone(phones.get(index));
            customer.setAddress(addresses.get(index));
            customer.setPostal_code(postalCodes.get(index));

            Set<Division> divisions = new HashSet<>(divisionRepository.findAll());
            if (!divisions.isEmpty()) {
                customer.setDivision(divisions.iterator().next());
            }

            customersToAdd.add(customer);
            customerRepository.save(customer);
        }

        System.out.println("Sample customers added successfully!");
    }
}

