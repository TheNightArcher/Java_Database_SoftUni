package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferRootSeedDto;
import softuni.exam.models.dto.OfferSeedDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.ApartmentType;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final AgentService agentService;
    private final ApartmentService apartmentService;
    private final ValidationUtil validationUtil;
    private static final String FILE_PATH = "src/main/resources/files/xml/offers.xml";

    public OfferServiceImpl(OfferRepository offerRepository, ModelMapper modelMapper, XmlParser xmlParser, AgentService agentService, ApartmentService apartmentService, ValidationUtil validationUtil) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.agentService = agentService;
        this.apartmentService = apartmentService;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder result = new StringBuilder();

        OfferRootSeedDto offers = this.xmlParser.fromFile(FILE_PATH, OfferRootSeedDto.class);

        offers.getOffers()
                .stream()
                .filter(offerSeedDto -> validateData(offerSeedDto, result))
                .map(this::mapToEntity)
                .forEach(this.offerRepository::save);

        return result.toString().trim();
    }

    private Offer mapToEntity(OfferSeedDto offerSeedDto) {
        Offer offer = this.modelMapper.map(offerSeedDto, Offer.class);
        offer.setAgent(this.agentService.findByFirstName(offerSeedDto.getAgent().getName()));
        offer.setApartment(this.apartmentService.findById(offerSeedDto.getApartment().getId()));
        return offer;
    }

    private boolean validateData(OfferSeedDto offerSeedDto, StringBuilder result) {
        if (!this.validationUtil.isValid(offerSeedDto)) {
            result.append("Invalid offer").append(System.lineSeparator());
            return false;
        }

        Agent agent = this.agentService.findByFirstName(offerSeedDto.getAgent().getName());
        if (agent == null) {
            result.append("Invalid offer").append(System.lineSeparator());
            return false;
        }
        result.append(String.format("Successfully imported offer %.2f", offerSeedDto.getPrice()))
                .append(System.lineSeparator());
        return true;
    }

    @Override
    public String exportOffers() {
        StringBuilder result = new StringBuilder();

        List<Offer> allOffers = this.offerRepository.findAllByApartmentTypeOrderedByAreaDescAndPriceAsc(ApartmentType.three_rooms);
        allOffers.forEach(o -> {
            result.append(String.format("Agent %s %s with offer №%d:\n" +
                    "   \t\t-Apartment area: %.2f\n" +
                    "   \t\t--Town: %s\n" +
                    "   \t\t---Price: %.2f$\n", o.getAgent().getFirstName(), o.getAgent().getLastName(),
                    o.getId(), o.getApartment().getArea(), o.getApartment().getTown().getTownName(),
                    o.getPrice()));
        });

        return result.toString().trim();
    }
}
