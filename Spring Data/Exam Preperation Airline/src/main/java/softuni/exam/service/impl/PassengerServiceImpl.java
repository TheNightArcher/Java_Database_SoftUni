package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PassengersSeedDTO;
import softuni.exam.models.entity.Passenger;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class PassengerServiceImpl implements PassengerService {

    private final static String PASSENGERS_FILE_PATH = "src/main/resources/files/json/passengers.json";

    private final PassengerRepository passengerRepository;
    private final TownService townService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public PassengerServiceImpl(PassengerRepository passengerRepository, TownService townService, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.passengerRepository = passengerRepository;
        this.townService = townService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return Files.readString(Path.of(PASSENGERS_FILE_PATH));
    }

    @Override
    public String importPassengers() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readPassengersFileContent(), PassengersSeedDTO[].class))
                .filter(passengersSeedDTO -> {
                    boolean isValid = validationUtil.isValid(passengersSeedDTO)
                            && !isPassengerExist(passengersSeedDTO.getEmail())
                            && townService.isTownExist(passengersSeedDTO.getTown());

                    sb.append(isValid ? String.format("Successfully imported Passenger %s - %s",
                                    passengersSeedDTO.getLastName(),
                                    passengersSeedDTO.getEmail())

                                    : "Invalid Passenger")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(passengersSeedDTO -> {
                    Passenger passenger = modelMapper.map(passengersSeedDTO, Passenger.class);

                    passenger.setTown(townService.getTown(passengersSeedDTO.getTown()));

                    return passenger;
                })
                .forEach(passengerRepository::save);

        return sb.toString().trim();
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        StringBuilder sb = new StringBuilder();
        passengerRepository.findAllByPassengerOrderByCount()
                .forEach(p -> sb.append(String.format("Passenger %s  %s\n" +
                                        "\tEmail - %s\n" +
                                        "\tPhone - %s\n" +
                                        "\tNumber of tickets - %d\n",
                                p.getFirstName(),
                                p.getLastName(),
                                p.getEmail(),
                                p.getPhoneNumber(),
                                p.getTickets().size()
                        ))
                        .append(System.lineSeparator()));

        return sb.toString();
    }

    @Override
    public Passenger getEmail(String email) {
        return passengerRepository.findByEmail(email);
    }

    @Override
    public boolean isPassengerExist(String email) {
        return passengerRepository.existsByEmail(email);
    }
}
