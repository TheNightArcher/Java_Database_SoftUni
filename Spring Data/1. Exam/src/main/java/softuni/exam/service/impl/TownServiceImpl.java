package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TownSeedDto;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private static final String FILE_PATH = "src/main/resources/files/json/towns.json";

    public TownServiceImpl(TownRepository townRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importTowns() throws IOException {
       StringBuilder result = new StringBuilder();

       Arrays.stream(this.gson
               .fromJson(readTownsFileContent(), TownSeedDto[].class))
               .filter(townSeedDto -> validateData(townSeedDto, result))
               .map(this::mapToEntity)
               .forEach(this.townRepository::save);

       return result.toString().trim();
    }

    @Override
    public Town findByTownName(String townName) {
        return this.townRepository.findByTownName(townName).orElse(null);
    }


    private Town mapToEntity(TownSeedDto townSeedDto) {
        return this.modelMapper.map(townSeedDto, Town.class);
    }

    private boolean validateData(TownSeedDto townSeedDto, StringBuilder result) {
        if (this.validationUtil.isValid(townSeedDto)){
            result.append(String.format("Successfully imported town %s - %d", townSeedDto.getTownName(), townSeedDto.getPopulation()))
                    .append(System.lineSeparator());
            return true;
        }
        result.append("Invalid town").append(System.lineSeparator());
        return false;
    }
}
