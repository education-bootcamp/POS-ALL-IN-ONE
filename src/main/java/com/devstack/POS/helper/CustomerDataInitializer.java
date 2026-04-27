package com.devstack.POS.helper;

import com.devstack.POS.dto.request.CustomerRequestDTO;
import com.devstack.POS.repo.CustomerRepo;
import com.devstack.POS.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerDataInitializer implements CommandLineRunner {

    private final CustomerService customerService;
    private final CustomerRepo customerRepo;  // injected to check existing data

    @Override
    public void run(String... args) throws Exception {
        if (customerRepo.count() > 0) {
            log.info("Customers already exist, skipping initialization.");
            return;
        }

        log.info("No customers found. Initializing 20 customers...");
        getInitialCustomers().forEach(customerService::createCustomer);
        log.info("Successfully initialized 20 customers.");
    }

    private List<CustomerRequestDTO> getInitialCustomers() {
        return List.of(
                new CustomerRequestDTO("Alice Johnson",   "123 Maple Street New York",         75000.00),
                new CustomerRequestDTO("Bob Smith",       "456 Oak Avenue Los Angeles",         82000.50),
                new CustomerRequestDTO("Carol Williams",  "789 Pine Road Chicago",              91000.75),
                new CustomerRequestDTO("David Brown",     "321 Elm Street Houston",             67000.00),
                new CustomerRequestDTO("Eva Martinez",    "654 Cedar Lane Phoenix",             53000.25),
                new CustomerRequestDTO("Frank Wilson",    "987 Birch Boulevard Philadelphia",   110000.00),
                new CustomerRequestDTO("Grace Taylor",    "147 Walnut Drive San Antonio",       48000.00),
                new CustomerRequestDTO("Henry Anderson",  "258 Chestnut Court San Diego",       95000.50),
                new CustomerRequestDTO("Isla Thomas",     "369 Spruce Way Dallas",              72000.00),
                new CustomerRequestDTO("Jack Jackson",    "741 Willow Path San Jose",           88000.75),
                new CustomerRequestDTO("Karen White",     "852 Ash Circle Austin",              63000.00),
                new CustomerRequestDTO("Liam Harris",     "963 Poplar Place Jacksonville",      57000.50),
                new CustomerRequestDTO("Mia Martin",      "159 Sycamore Square Fort Worth",     104000.00),
                new CustomerRequestDTO("Noah Garcia",     "357 Magnolia Terrace Columbus",      79000.25),
                new CustomerRequestDTO("Olivia Lee",      "486 Redwood Rise Charlotte",         86000.00),
                new CustomerRequestDTO("Paul Walker",     "624 Sequoia Street Indianapolis",    92000.50),
                new CustomerRequestDTO("Quinn Hall",      "753 Juniper Junction San Francisco", 115000.75),
                new CustomerRequestDTO("Rachel Young",    "861 Hickory Heights Seattle",        68000.00),
                new CustomerRequestDTO("Samuel King",     "972 Cypress Crescent Denver",        74000.25),
                new CustomerRequestDTO("Tina Scott",      "183 Palmetto Parade Nashville",      59000.00)
        );
    }
}