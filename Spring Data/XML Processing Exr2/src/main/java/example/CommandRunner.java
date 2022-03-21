package example;

import example.models.dtos.CategorySeedRootDto;
import example.models.dtos.ProductSeedRootDto;
import example.models.dtos.ProductsViewRootDto;
import example.models.dtos.UserSeedRootDto;
import example.services.CategoryService;
import example.services.ProductService;
import example.services.UserService;
import example.utils.XmlParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

@Component
public class CommandRunner implements CommandLineRunner {

    public static final String RESOURCE_FILE_PATH = "src/main/resources/files/";
    public static final String OUT_FILE_PATH = "out/";
    public static final String CATEGORIES_FILE_PATH = "categories.xml";
    public static final String USERS_FILE_PATH = "users.xml";
    public static final String PRODUCTS_FILE_PATH = "products.xml";
    public static final String PRODUCTS_IN_RANGE = "products-in-range.xml";


    private final XmlParser xmlParser;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final BufferedReader bufferedReader;

    public CommandRunner(XmlParser xmlParser, CategoryService categoryService, UserService userService, ProductService productService) {
        this.xmlParser = xmlParser;
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }


    @Override
    public void run(String... args) throws Exception {
        SeedData();

        System.out.println("Please enter ex: ");
        int exNum = Integer.parseInt(bufferedReader.readLine());

        switch (exNum){

            case 1 -> productsInRange();
        }
    }

    private void productsInRange() throws JAXBException {
        ProductsViewRootDto rootDto = productService
                .findProductsInRangeWithOutBuyer();

        xmlParser.writeToFile(RESOURCE_FILE_PATH + OUT_FILE_PATH + PRODUCTS_IN_RANGE,rootDto);
    }

    private void SeedData() throws JAXBException, FileNotFoundException {
        if (categoryService.getCount() == 0){
            CategorySeedRootDto categorySeedRootDto = xmlParser
                    .fromFile(RESOURCE_FILE_PATH + CATEGORIES_FILE_PATH, CategorySeedRootDto.class);

            categoryService.seedCategories(categorySeedRootDto.getCategories());
        }

        if (userService.getCount() == 0){
            UserSeedRootDto userSeedRootDto = xmlParser
                    .fromFile(RESOURCE_FILE_PATH + USERS_FILE_PATH, UserSeedRootDto.class);

            userService.seedUsers(userSeedRootDto.getUsers());
        }

        if (productService.getCount() == 0){
            ProductSeedRootDto productSeedRootDto = xmlParser
                    .fromFile(RESOURCE_FILE_PATH + PRODUCTS_FILE_PATH, ProductSeedRootDto.class);

            productService.seedProducts(productSeedRootDto.getProducts());
        }
    }
}
