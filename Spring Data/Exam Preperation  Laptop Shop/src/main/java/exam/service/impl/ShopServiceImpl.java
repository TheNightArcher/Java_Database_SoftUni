package exam.service.impl;

import exam.model.dto.ShopSeedRootDTO;
import exam.model.entity.Shop;
import exam.repository.ShopRepository;
import exam.service.ShopService;
import exam.service.TownService;
import exam.util.ValidationUtil;
import exam.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ShopServiceImpl implements ShopService {

    private static final String SHOPS_FILE_PATH = "src/main/resources/files/xml/shops.xml";

    private final ShopRepository shopRepository;
    private final TownService townService;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    @Autowired
    public ShopServiceImpl(ShopRepository shopRepository, TownService townService, ValidationUtil validationUtil, ModelMapper modelMapper, XmlParser xmlParser) {
        this.shopRepository = shopRepository;
        this.townService = townService;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return shopRepository.count() > 0;
    }

    @Override
    public String readShopsFileContent() throws IOException {
        return Files.readString(Path.of(SHOPS_FILE_PATH));
    }

    @Override
    public String importShops() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(SHOPS_FILE_PATH, ShopSeedRootDTO.class)
                .getShops()
                .stream()
                .filter(shopSeedDTO -> {
                    boolean isValid = validationUtil.isValid(shopSeedDTO);

                    if (shopRepository.findByName(shopSeedDTO.getName()) != null) {

                        sb.append("Invalid shop")
                                .append(System.lineSeparator());

                        return false;
                    } else {

                        sb.append(isValid ? String.format("Successfully imported Shop %s - %.0f",
                                        shopSeedDTO.getName(),
                                        shopSeedDTO.getIncome())

                                        : "Invalid shop")
                                .append(System.lineSeparator());

                        return isValid;
                    }


                })
                .map(shopSeedDTO -> {
                    Shop shop = modelMapper.map(shopSeedDTO, Shop.class);


                    shop.setTown(townService.getTown(shopSeedDTO.getTown().getName()));

                    return shop;
                })
                .forEach(shopRepository::save);

        return sb.toString().trim();
    }

    @Override
    public Shop getShop(String name) {
        return shopRepository.findByName(name);
    }
}
