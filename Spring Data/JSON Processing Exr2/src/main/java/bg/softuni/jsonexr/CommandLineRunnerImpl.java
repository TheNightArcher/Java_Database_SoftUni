package bg.softuni.jsonexr;

import bg.softuni.jsonexr.models.dtos.CategoryProductsDto;
import bg.softuni.jsonexr.models.dtos.ProductNameAndPriceDto;
import bg.softuni.jsonexr.models.dtos.UserSoldDto;
import bg.softuni.jsonexr.models.dtos.UsersWithMoreThenOneSoledProductDto;
import bg.softuni.jsonexr.services.CategoryService;
import bg.softuni.jsonexr.services.ProductService;
import bg.softuni.jsonexr.services.UserService;
import com.google.gson.Gson;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    public static final String OUTPUT_PATH = "src/main/resources/files/out/";

    public static final String PRODUCT_IN_RANGE_FILE_NAME = "products-in-range.json";
    public static final String USER_AND_SOLD_PRODUCTS = "users-and-sold-products.json";
    public static final String CATEGORIES_WITH_COUNT_OF_PRODUCTS = "categories-with-count-of-products.json";
    public static final String USERS_WITH_PRODUCTS = "users-with-products.json";

    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final BufferedReader bufferedReader;
    private final Gson gson;

    public CommandLineRunnerImpl(CategoryService categoryService, UserService userService, ProductService productService, Gson gson) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.gson = gson;
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(String... args) throws Exception {
        seedDate();

        System.out.println("Pleas enter Exr number:");
        int exNumber = Integer.parseInt(bufferedReader.readLine());

        switch (exNumber) {
            case 1 -> productsInRange();
            case 2 -> soldProducts();
            case 3 -> categoriesByCount();
            case 4 -> usersProducts();
        }
        
    }

    private void usersProducts() throws IOException {
        List<UsersWithMoreThenOneSoledProductDto> allUsersWithMoreThenOneSoledProduct = userService
                .findAllUsersWithMoreThenOneSoledProduct();

        String content = gson.toJson(allUsersWithMoreThenOneSoledProduct);

        writeToFile(OUTPUT_PATH + USERS_WITH_PRODUCTS,content);
    }

    private void categoriesByCount() throws IOException {
        List<CategoryProductsDto> categoryProductsDtos = categoryService
                .findAllCategoriesWithProductCount();

        String content = gson.toJson(categoryProductsDtos);

        writeToFile(OUTPUT_PATH + CATEGORIES_WITH_COUNT_OF_PRODUCTS,content );
    }

    private void soldProducts() throws IOException {
        List<UserSoldDto> userSoldDtos =
                userService.findAllUserWithMoreThenOneProducts();


        String content = gson.toJson(userSoldDtos);

        writeToFile(OUTPUT_PATH + USER_AND_SOLD_PRODUCTS, content);
    }

    private void productsInRange() throws IOException {
        List<ProductNameAndPriceDto> productsDtos = productService
                .findAllProductsInRangeByPriceOrderByPrice(BigDecimal.valueOf(500L), BigDecimal.valueOf(1000L));

        String content = gson.toJson(productsDtos);

        writeToFile(OUTPUT_PATH + PRODUCT_IN_RANGE_FILE_NAME, content);
    }

    private void writeToFile(String filePath, String content) throws IOException {
        Files.write(Path.of(filePath), Collections.singleton(content));
    }

    private void seedDate() throws IOException {
        categoryService.seedCategories();
        userService.seedUsers();
        productService.seedProducts();
    }
}
