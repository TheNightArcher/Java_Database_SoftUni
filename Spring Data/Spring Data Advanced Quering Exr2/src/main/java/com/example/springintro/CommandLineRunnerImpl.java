package com.example.springintro;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.EditionType;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.Locale;
import java.util.Scanner;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        //int input = Integer.parseInt(scanner.nextLine());

        // From "Ctrl + /" you can uncomment my exercises ;P


//        11. Exr
//        this.bookService.findGivenTitle("Things Fall Apart");

//        10. Exr
//        this.authorService.getAllCopies()
//                .forEach(a -> System.out.printf("%s %s - %d\n",
//                        a.getFirstName(),
//                        a.getLastName(),
//                        a.getCopies()));

//        09. Exr
//        int count = this.bookService.findAllTitlesBiggerThenGivenLength(12);
//        System.out.println(count);

//        08. Exr
//        this.bookService.findAllAuthorsWithLastNameLike("Ric");

//        07. Exr
//        this.bookService.findAllTitlesContainsGivenLetters("sK");

//        06. Exr
//        this.authorService.getAllNamesEndLikeGivenLetters("e");

//        05. Exr
//        this.bookService.findAllBooksBeforeGivenDate("12-04-1992");

//        04. Exr
//        this.bookService.findAllBooksNotInGivenYear(2000);

//        03. Exr
//        this.bookService.findAllBooksPriceBiggerOrLowerThenGiven(BigDecimal.valueOf(5),BigDecimal.valueOf(40));

//        02. Exr
//        this.bookService.findAllBooksByEditionTypeAndLessThenGivenCopies(EditionType.GOLD,5000);

//        01.Exr
//        this.bookService.
//                findAllBooksByAgeRestriction(AgeRestriction.valueOf(input.toUpperCase(Locale.ROOT)));

    }

    private void printALlBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        bookService
                .findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsAndNumberOfTheirBooks() {
        authorService
                .getAllAuthorsOrderByCountOfTheirBooks()
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(int year) {
        bookService
                .findAllAuthorsWithBooksWithReleaseDateBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksAfterYear(int year) {
        bookService
                .findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }
}
