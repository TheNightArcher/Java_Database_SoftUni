package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xml.SellersSeedRootDTO;
import softuni.exam.models.entity.Rating;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class SellerServiceImpl implements SellerService {

    private final static String SELLERS_FILE_PATH = "src/main/resources/files/xml/sellers.xml";

    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.sellerRepository = sellerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return Files.readString(Path.of(SELLERS_FILE_PATH));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(SELLERS_FILE_PATH, SellersSeedRootDTO.class)
                .getSellers()
                .stream()
                .filter(sellerSeedDTO -> {
                    boolean isValid = validationUtil.isValid(sellerSeedDTO)
                            && !isEmailExists(sellerSeedDTO.getEmail())
                            && isRatingExists(sellerSeedDTO.getRating());

                    sb.append(isValid ? String.format("Successfully import seller %s - %s",
                                    sellerSeedDTO.getLastName(),
                                    sellerSeedDTO.getEmail())

                                    : "Invalid seller")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(sellerSeedDTO -> {

                    Seller seller = modelMapper.map(sellerSeedDTO, Seller.class);

                    seller.setRating(Rating.valueOf(sellerSeedDTO.getRating()));

                    return seller;
                })
                .forEach(sellerRepository::save);

        return sb.toString().trim();
    }

    @Override
    public Seller getSeller(long id) {
        return sellerRepository
                .findById(id).orElse(null);
    }

    @Override
    public boolean isSellerExists(long id) {
        return sellerRepository.existsById(id);
    }

    private boolean isEmailExists(String email) {
        return sellerRepository.existsByEmail(email);
    }

    private boolean isRatingExists(String rating) {

        //GOOD,BAD,UNKNOWN

        boolean isPresent = false;

        switch (rating) {
            case "GOOD":
            case "UNKNOWN":
            case "BAD":
                isPresent = true;
                break;
        }

        return isPresent;
    }
}
