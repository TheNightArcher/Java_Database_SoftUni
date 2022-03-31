package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.CarSeedDTO;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CarServiceImpl implements CarService {

    private final static String CARS_FILE_PATH = "src/main/resources/files/json/cars.json";

    private final CarRepository carRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.carRepository = carRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return Files.readString(Path.of(CARS_FILE_PATH));
    }

    @Override
    public String importCars() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readCarsFileContent(), CarSeedDTO[].class))
                .filter(carSeedDTO -> {
                    boolean isValid = validationUtil.isValid(carSeedDTO);

                    sb.append(isValid ? String.format("Successfully imported car - %s - %s",
                                    carSeedDTO.getMake(),
                                    carSeedDTO.getModel())

                                    : "Invalid car")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(carSeedDTO -> modelMapper.map(carSeedDTO, Car.class))
                .forEach(carRepository::save);

        return sb.toString().trim();
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        StringBuilder sb = new StringBuilder();

        carRepository.findCarsOrderByPicture()
                .forEach(c -> sb.append(String.format("Car make - %s, model - %s\n" +
                        "\tKilometers - %d\n" +
                        "\tRegistered on - %s\n" +
                        "\tNumber of pictures - %d\n",
                        c.getMake(),
                        c.getModel(),
                        c.getKilometers(),
                        c.getRegisteredOn(),
                        c.getPictures().size()))
                        .append(System.lineSeparator()));

        return sb.toString().trim();
    }

    @Override
    public Car getCar(long car) {
        return carRepository.findById(car).orElse(null);
    }

    @Override
    public boolean isCarExists(long id) {
        return carRepository.existsById(id);
    }
}
