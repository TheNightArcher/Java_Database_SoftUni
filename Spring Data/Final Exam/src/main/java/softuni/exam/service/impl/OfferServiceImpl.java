package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xml.OfferSeedRootDTO;
import softuni.exam.models.entity.Agent;
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

@Service
public class OfferServiceImpl implements OfferService {

    private final static String OFFERS_FILE_PATH = "src/main/resources/files/xml/offers.xml";
    private final static String THREE_ROOMS = "three_rooms";

    private final OfferRepository offerRepository;
    private final AgentService agentService;
    private final ApartmentService apartmentService;

    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, AgentService agentService, ApartmentService apartmentService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.offerRepository = offerRepository;
        this.agentService = agentService;
        this.apartmentService = apartmentService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(OFFERS_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(OFFERS_FILE_PATH, OfferSeedRootDTO.class)
                .getOffers()
                .stream()
                .filter(offerSeedDTO -> {
                    boolean isValid = validationUtil.isValid(offerSeedDTO)
                            && isExistsAgent(offerSeedDTO.getAgent().getName())
                            && isExistsApartment(offerSeedDTO.getApartment().getId())
                            && !isExistsAgentName(agentService.findAgent(offerSeedDTO.getAgent().getName()));

                    sb.append(isValid ? String.format("Successfully imported offer %.2f",
                                    offerSeedDTO.getPrice())

                                    : "Invalid offer")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(offerSeedDTO -> {
                    Offer offer = modelMapper.map(offerSeedDTO, Offer.class);

                    offer.setAgent(agentService.findAgent(offerSeedDTO.getAgent().getName()));
                    offer.setApartment(apartmentService.findApartment(offerSeedDTO.getApartment().getId()));

                    return offer;
                })
                .forEach(offerRepository::save);

        return sb.toString().trim();
    }

    private boolean isExistsApartment(long id) {
        return apartmentService.isApartmentExists(id);
    }

    private boolean isExistsAgent(String name) {
        return agentService.isAgentExists(name);
    }

    private boolean isExistsAgentName(Agent agent) {
        return offerRepository.existsByAgent(agent);
    }

    @Override
    public String exportOffers() {
        StringBuilder sb = new StringBuilder();

        offerRepository.findAllOffersOfThreeRoomsApartments()
                .forEach(o -> {
                    if (o.getApartment().getApartmentType().toString().equals(THREE_ROOMS)) {
                        sb.append(String.format("""
                                        Agent %s %s with offer №%d:
                                           \t\t-Apartment area: %.2f
                                           \t\t--Town: %s
                                           \t\t---Price: %.2f$
                                        """,
                                o.getAgent().getFirstName(),
                                o.getAgent().getLastName(),
                                o.getId(),
                                o.getApartment().getArea(),
                                o.getApartment().getTown().getTownName(),
                                o.getPrice()));
                    }
                });

        return sb.toString().trim();
    }
}
