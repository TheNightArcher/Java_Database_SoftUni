package com.example.springintro.service.impl;

import com.example.springintro.model.entity.*;
import com.example.springintro.repository.BookRepository;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private static final String BOOKS_FILE_PATH = "src/main/resources/files/books.txt";

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedBooks() throws IOException {
        if (bookRepository.count() > 0) {
            return;
        }

        Files
                .readAllLines(Path.of(BOOKS_FILE_PATH))
                .forEach(row -> {
                    String[] bookInfo = row.split("\\s+");

                    Book book = createBookFromInfo(bookInfo);

                    bookRepository.save(book);
                });
    }

    @Override
    public List<Book> findAllBooksAfterYear(int year) {
        return bookRepository
                .findAllByReleaseDateAfter(LocalDate.of(year, 12, 31));
    }

    @Override
    public List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year) {
        return bookRepository
                .findAllByReleaseDateBefore(LocalDate.of(year, 1, 1))
                .stream()
                .map(book -> String.format("%s %s", book.getAuthor().getFirstName(),
                        book.getAuthor().getLastName()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName) {
        return bookRepository
                .findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(firstName, lastName)
                .stream()
                .map(book -> String.format("%s %s %d",
                        book.getTitle(),
                        book.getReleaseDate(),
                        book.getCopies()))
                .collect(Collectors.toList());
    }

    @Override
    public void findAllBooksByAgeRestriction(AgeRestriction type) {

        this.bookRepository.findByAgeRestriction(type)
                .stream().
                map(Book::getTitle)
                .forEach(System.out::println);
    }

    @Override
    public void findAllBooksByEditionTypeAndLessThenGivenCopies(EditionType type, int copies) {
        this.bookRepository.findByEditionTypeAndCopiesLessThan(type, copies)
                .stream().map(Book::getTitle)
                .forEach(System.out::println);
    }

    @Override
    public void findAllBooksPriceBiggerOrLowerThenGiven(BigDecimal lower, BigDecimal higher) {
        this.bookRepository.findByPriceLessThanOrPriceGreaterThan(lower, higher)
                .forEach(b -> System.out.printf("%s - $%.2f\n",
                        b.getTitle(),
                        b.getPrice()));
    }

    @Override
    public void findAllBooksNotInGivenYear(int year) {

        LocalDate before = LocalDate.of(year, 1, 1);
        LocalDate after = LocalDate.of(year, 12, 31);

        this.bookRepository.findByReleaseDateBeforeOrReleaseDateAfter(before, after)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    @Override
    public void findAllBooksBeforeGivenDate(String date) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate givenDate = LocalDate.parse(date, dateTimeFormatter);

        this.bookRepository.findByReleaseDateBefore(givenDate)
                .forEach(b -> System.out.printf("%s %s %.2f\n",
                        b.getTitle(),
                        b.getEditionType(),
                        b.getPrice()));
    }

    @Override
    public void findAllTitlesContainsGivenLetters(String letters) {
        this.bookRepository.findByTitleContaining(letters)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    @Override
    public void findAllAuthorsWithLastNameLike(String letters) {
        this.bookRepository.findByAuthorLastNameStartingWith(letters)
                .forEach(b -> System.out.printf("%s (%s %s)\n",
                        b.getTitle(),
                        b.getAuthor().getFirstName(),
                        b.getAuthor().getLastName()));
    }

    @Override
    public int findAllTitlesBiggerThenGivenLength(int length) {
        return this.bookRepository.findByTitleGreaterThanGivenLength(length);
    }

    @Override
    public void findGivenTitle(String title) {
        this.bookRepository.findInformationAboutGivenBook(title)
                .forEach(b -> System.out.printf("%s %S %S %.2f\n",
                        b.getTitle(),
                        b.getEditionType(),
                        b.getAgeRestriction(),
                        b.getPrice()));
    }

    private Book createBookFromInfo(String[] bookInfo) {
        EditionType editionType = EditionType.values()[Integer.parseInt(bookInfo[0])];
        LocalDate releaseDate = LocalDate
                .parse(bookInfo[1], DateTimeFormatter.ofPattern("d/M/yyyy"));
        Integer copies = Integer.parseInt(bookInfo[2]);
        BigDecimal price = new BigDecimal(bookInfo[3]);
        AgeRestriction ageRestriction = AgeRestriction
                .values()[Integer.parseInt(bookInfo[4])];
        String title = Arrays.stream(bookInfo)
                .skip(5)
                .collect(Collectors.joining(" "));

        Author author = authorService.getRandomAuthor();
        Set<Category> categories = categoryService
                .getRandomCategories();

        return new Book(editionType, releaseDate, copies, price, ageRestriction, title, author, categories);
    }


}
