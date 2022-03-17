package bg.softuni.jsonexr;

import bg.softuni.jsonexr.services.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;

    public CommandLineRunnerImpl(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        
        seedDate();
    }

    private void seedDate() throws IOException {
        categoryService.seedCategories();
    }
}
