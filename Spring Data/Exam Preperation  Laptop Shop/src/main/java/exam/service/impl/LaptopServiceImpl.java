package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.LaptopSeedDTO;
import exam.model.entity.Laptop;
import exam.repository.LaptopRepository;
import exam.service.LaptopService;
import exam.service.ShopService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class LaptopServiceImpl implements LaptopService {

    private static final String LAPTOPS_FILE_PATH = "src/main/resources/files/json/laptops.json";

    private final LaptopRepository laptopRepository;
    private final ShopService shopService;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;

    @Autowired
    public LaptopServiceImpl(LaptopRepository laptopRepository, ShopService shopService, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson) {
        this.laptopRepository = laptopRepository;
        this.shopService = shopService;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return laptopRepository.count() > 0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        return Files.readString(Path.of(LAPTOPS_FILE_PATH));
    }

    @Override
    public String importLaptops() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readLaptopsFileContent(), LaptopSeedDTO[].class))
                .filter(laptopSeedDTO -> {
                    boolean isValid = validationUtil.isValid(laptopSeedDTO);

                    //BASIC, PREMIUM, LIFETIME.
                    return isValid(sb, laptopSeedDTO, isValid);

                })
                .map(laptopSeedDTO -> {
                    Laptop laptop = modelMapper.map(laptopSeedDTO, Laptop.class);

                    laptop.setShop(shopService.getShop(laptopSeedDTO.getShop().getName()));

                    return laptop;
                })
                .forEach(laptopRepository::save);

        return sb.toString().trim();
    }

    @Override
    public String exportBestLaptops() {
        StringBuilder sb = new StringBuilder();

        laptopRepository.findTheBestLaptops()
                .forEach(l -> sb.append(String.format("""
                                        Laptop - %s
                                        *Cpu speed - %.2f
                                        **Ram - %d
                                        ***Storage - %d
                                        ****Price - %.2f
                                        #Shop name - %s
                                        ##Town - %s
                                        """,
                                l.getMacAddress(),
                                l.getCpuSpeed(),
                                l.getRam(),
                                l.getStorage(),
                                l.getPrice(),
                                l.getShop().getName(),
                                l.getShop().getTown()))
                        .append(System.lineSeparator()));

        return sb.toString().trim();
    }

    private boolean isValid(StringBuilder sb, LaptopSeedDTO laptopSeedDTO, boolean isValid) {
        if (!laptopSeedDTO.getWarrantyType().equals("BASIC") &&
                !laptopSeedDTO.getWarrantyType().equals("PREMIUM") &&
                !laptopSeedDTO.getWarrantyType().equals("LIFETIME")) {

            sb.append("Invalid Laptop")
                    .append(System.lineSeparator());

            return false;
        } else if (laptopRepository.findByMacAddress(laptopSeedDTO.getMacAddress()) != null) {

            sb.append("Invalid Laptop")
                    .append(System.lineSeparator());

            return false;

        } else {

            sb.append(isValid ? String.format("Successfully imported Laptop %s - %.2f - %d - %d",
                            laptopSeedDTO.getMacAddress(),
                            laptopSeedDTO.getCpuSpeed(),
                            laptopSeedDTO.getRam(),
                            laptopSeedDTO.getStorage())

                            : "Invalid Laptop")
                    .append(System.lineSeparator());

            return isValid;
        }
    }
}
