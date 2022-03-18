package bg.softuni.jsonexr.services.impl;

import bg.softuni.jsonexr.constants.GlobalConstants;
import bg.softuni.jsonexr.models.Product;
import bg.softuni.jsonexr.models.dtos.ProductNameAndPriceDto;
import bg.softuni.jsonexr.models.dtos.ProductSeedDto;
import bg.softuni.jsonexr.repositories.ProductRepository;
import bg.softuni.jsonexr.services.CategoryService;
import bg.softuni.jsonexr.services.ProductService;
import bg.softuni.jsonexr.services.UserService;
import bg.softuni.jsonexr.utils.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String PRODUCTS_FILE_NAME = "products.json";

    private final ProductRepository productRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final UserService userService;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson, UserService userService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.userService = userService;
        this.categoryService = categoryService;
    }


    @Override
    public void seedProducts() throws IOException {

        if (productRepository.count() == 0) {
            String fileContent = Files
                    .readString(Path.of(GlobalConstants.RESOURCE_FILE_PATH + PRODUCTS_FILE_NAME));

            ProductSeedDto[] productSeedDtos = gson.fromJson(fileContent, ProductSeedDto[].class);


            Arrays.stream(productSeedDtos)
                    .filter(validationUtil::isValid)
                    .map(productSeedDto -> {
                        Product product = modelMapper.map(productSeedDto, Product.class);
                        product.setSeller(userService.findRandomUser());

                        if (product.getPrice().compareTo(BigDecimal.valueOf(900L)) > 0) {
                            product.setBuyer(userService.findRandomUser());
                        }

                        product.setCategories(categoryService.findRandomCategories());

                        return product;
                    })
                    .forEach(productRepository::save);
        }
    }

    @Override
    public List<ProductNameAndPriceDto> findAllProductsInRangeByPriceOrderByPrice(BigDecimal lower, BigDecimal upper) {
        return productRepository.findAllByPriceBetweenAndBuyerIsNullOrderByPrice(lower, upper)
                .stream()
                .map(product -> {
                    ProductNameAndPriceDto productNameAndPriceDto = modelMapper
                            .map(product, ProductNameAndPriceDto.class);

                    productNameAndPriceDto.setSeller(String.format("%s %s",
                            product.getSeller().getFirstName(),
                            product.getSeller().getLastName()));

                    return productNameAndPriceDto;
                })
                .collect(Collectors.toList());
    }
}
