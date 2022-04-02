package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ApartmentRootSeedDto;
import softuni.exam.models.dto.ApartmentSeedDto;
import softuni.exam.models.entity.Apartment;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final TownService townService;
    private static final String FILE_PATH = "src/main/resources/files/xml/apartments.xml";

    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil, TownService townService) {
        this.apartmentRepository = apartmentRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return this.apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        StringBuilder result = new StringBuilder();

        ApartmentRootSeedDto apartments = this.xmlParser.fromFile(FILE_PATH, ApartmentRootSeedDto.class);

        apartments.getApartments()
                .stream()
                .filter(apartmentSeedDto -> validateData(apartmentSeedDto, result))
                .map(this::mapToEntity)
                .forEach(this.apartmentRepository::save);

        return result.toString().trim();
    }

    @Override
    public Apartment findById(Long id) {
        return this.apartmentRepository.findById(id).orElse(null);
    }

    private Apartment mapToEntity(ApartmentSeedDto apartmentSeedDto) {
        Apartment apartment = this.modelMapper.map(apartmentSeedDto, Apartment.class);
        apartment.setTown(this.townService.findByTownName(apartmentSeedDto.getTown()));
        return apartment;
    }

    private boolean validateData(ApartmentSeedDto apartmentSeedDto, StringBuilder result) {
        if (!this.validationUtil.isValid(apartmentSeedDto)){
            result.append("Invalid apartment").append(System.lineSeparator());
            return false;
        }
        Apartment byTownNameAndArea = this.apartmentRepository.findByTownNameAndArea(apartmentSeedDto.getTown(), apartmentSeedDto.getArea()).orElse(null);
        if (byTownNameAndArea != null){
            result.append("Invalid apartment").append(System.lineSeparator());
            return false;
        }

        result.append(String.format("Successfully imported apartment %s - %.2f", apartmentSeedDto.getApartmentType(), apartmentSeedDto.getArea()))
                .append(System.lineSeparator());
        return true;
    }
}
