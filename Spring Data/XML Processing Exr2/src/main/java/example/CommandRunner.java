package example;

import example.models.dtos.CategorySeedRootDto;
import example.services.CategoryService;
import example.utils.XmlParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

@Component
public class CommandRunner implements CommandLineRunner {

    public static final String RESOURCE_FILE_PATH = "src/main/resources/files/";
    public static final String CATEGORIES_FILE_PATH = "categories.xml";


    private final XmlParser xmlParser;
    private final CategoryService categoryService;

    public CommandRunner(XmlParser xmlParser, CategoryService categoryService) {
        this.xmlParser = xmlParser;
        this.categoryService = categoryService;
    }


    @Override
    public void run(String... args) throws Exception {
        SeedData();
    }

    private void SeedData() throws JAXBException, FileNotFoundException {
        CategorySeedRootDto categorySeedRootDto = xmlParser
                .fromFile(RESOURCE_FILE_PATH + CATEGORIES_FILE_PATH, CategorySeedRootDto.class);

        categoryService.seedCategories(categorySeedRootDto.getCategories());
    }
}
