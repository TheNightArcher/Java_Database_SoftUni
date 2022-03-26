package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.CustomerSeedDTO;
import exam.model.entity.Customer;
import exam.repository.CustomerRepository;
import exam.service.CustomerService;
import exam.service.TownService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final String CUSTOMERS_FILE_PATH = "src/main/resources/files/json/customers.json";

    private final CustomerRepository customerRepository;
    private final TownService townService;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, TownService townService, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson) {
        this.customerRepository = customerRepository;
        this.townService = townService;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return customerRepository.count() > 0;
    }

    @Override
    public String readCustomersFileContent() throws IOException {
        return Files.readString(Path.of(CUSTOMERS_FILE_PATH));
    }

    @Override
    public String importCustomers() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readCustomersFileContent(), CustomerSeedDTO[].class))
                .filter(customerSeedDTO -> {
                    boolean isValid = validationUtil.isValid(customerSeedDTO);

                    sb.append(isValid ? String.format("Successfully imported Customer %s %s - %s",
                                    customerSeedDTO.getFirstName(),
                                    customerSeedDTO.getLastName(),
                                    customerSeedDTO.getEmail())

                                    : "Invalid Customer")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(customerSeedDTO -> {
                    Customer customer = modelMapper.map(customerSeedDTO, Customer.class);

                    customer.setTown(townService.getTown(customerSeedDTO.getTown().getName()));

                    return customer;
                })
                .forEach(customerRepository::save);

        return sb.toString().trim();
    }
}
