package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xml.OfferSeedRootDTO;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.OfferService;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class OfferServiceImpl implements OfferService {

    private final static String OFFER_FILE_PATH = "src/main/resources/files/xml/offers.xml";

    private final OfferRepository offerRepository;
    private final CarService carService;
    private final SellerService sellerService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, CarService carService, SellerService sellerService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.offerRepository = offerRepository;
        this.carService = carService;
        this.sellerService = sellerService;
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
        return Files.readString(Path.of(OFFER_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(OFFER_FILE_PATH, OfferSeedRootDTO.class)
                .getOffers()
                .stream()
                .filter(offerSeedDTO -> {
                    boolean isValid = validationUtil.isValid(offerSeedDTO)
                            && carService.isCarExists(offerSeedDTO.getCar().getId())
                            && sellerService.isSellerExists(offerSeedDTO.getSeller().getId());

                    sb.append(isValid ? String.format("Successfully import offer %s - %s",
                            offerSeedDTO.getAddedOn(),
                            offerSeedDTO.isHasGoldStatus())

                            :"Invalid offer")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(offerSeedDTO -> {
                    Offer offer = modelMapper.map(offerSeedDTO, Offer.class);


                    offer.setCar(carService.getCar(offerSeedDTO.getCar().getId()));
                    offer.setSeller(sellerService.getSeller(offerSeedDTO.getSeller().getId()));

                    return offer;
                })
                .forEach(offerRepository::save);

        return sb.toString().trim();
    }
}
