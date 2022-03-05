package bg.softuni.services;

import bg.softuni.models.*;
import bg.softuni.repositories.AuthorRepository;
import bg.softuni.repositories.BookRepository;
import bg.softuni.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SeedServiceImpl implements SeedService {
    private static final String RESOURCE_PATH = "src\\main\\resources\\files";
    private static final String AUTHORS_PATH = RESOURCE_PATH + "\\authors.txt";
    private static final String CATEGORIES_PATH = RESOURCE_PATH + "\\categories.txt";
    private static final String BOOKS_PATH = RESOURCE_PATH + "\\books.txt";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private  AuthorService authorService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public void seedAuthors() throws IOException {
        Files.readAllLines(Path.of(AUTHORS_PATH))
                .stream()
                .filter(a -> !a.isBlank())
                .map(a -> a.split("\\s+"))
                .map(names -> new Author(names[0], names[1]))
                .forEach(authorRepository::save);
    }

    @Override
    public void seedCategories() throws IOException {
        Files.readAllLines(Path.of(CATEGORIES_PATH))
                .stream()
                .filter(a -> !a.isBlank())
                .map(Category::new)
                .forEach(categoryRepository::save);
    }

    @Override
    public void seedBooks() throws IOException {
        Files.readAllLines(Path.of(BOOKS_PATH))
                .stream()
                .filter(a -> !a.isBlank())
                .map(this::getBookObject)
                .forEach(bookRepository::save);
    }

    private Book getBookObject(String line) {
        String[] bookParts = line.split("\\s+");

        int editionTypeIndex = Integer.parseInt(bookParts[0]);
        EditionType editionType = EditionType.values()[editionTypeIndex];

        LocalDate ageRelease = LocalDate.parse(bookParts[1],
                DateTimeFormatter.ofPattern("d/M/yyyy"));

        int copies = Integer.parseInt(bookParts[2]);

        BigDecimal price = new BigDecimal(bookParts[3]);

        int ageRegistrationIndex = Integer.parseInt(bookParts[4]);
        AgeRegistration ageRegistration = AgeRegistration
                .values()[ageRegistrationIndex];

        String title = Arrays.stream(bookParts)
                .skip(5)
                .collect(Collectors.joining(" "));

        Author author = authorService.getRandomAuthor();
        Set<Category> categories = categoryService.getRandomCategories();


        return new Book(title,editionType,price,copies,ageRelease
                ,ageRegistration,author,categories);
    }
}
