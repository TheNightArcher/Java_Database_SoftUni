package bg.softuni;

import bg.softuni.models.Author;
import bg.softuni.models.Book;
import bg.softuni.repositories.AuthorRepository;
import bg.softuni.repositories.BookRepository;
import bg.softuni.services.SeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final SeedService seedService;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public ConsoleRunner(SeedService seedService, AuthorRepository authorRepository, BookRepository bookRepository) {
        this.seedService = seedService;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // this.seedService.seedAll();
        // this.getAllBooksAfter2000();
        // this.getAllAuthorsBefore1990MoreThenOneBook();
        // this.getAllAuthorsOrderThemDescBySize();
        this.getAllBooksFromAuthor();
    }

    private void getAllBooksFromAuthor() {
        String[] author = "George Powell".split("\\s+");

        String firstName = author[0];
        String lastName = author[1];

        int id = this.authorRepository.findByFirstNameAndLastName(firstName,lastName).getId();

        Set<Book> booksByAuthor = this.bookRepository.findByAuthorId(id);

        booksByAuthor.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .sorted((l, r) -> r.getReleaseDate().compareTo(l.getReleaseDate()))
                .forEach(b -> System.out.printf("%s %S -> %d\n",
                        b.getTitle(),
                        b.getReleaseDate(),
                        b.getCopies()));
    }

    private void getAllAuthorsOrderThemDescBySize() {
        List<Author> authors = this.authorRepository.findAll();

        authors.stream()
                .sorted((l, r) -> r.getBooks().size() - l.getBooks().size())
                .forEach(a -> System.out.printf("%s %s -> %d\n",
                        a.getFirstName(),
                        a.getLastName(),
                        a.getBooks().size()));
    }

    private void getAllAuthorsBefore1990MoreThenOneBook() {
        LocalDate beforeDate = LocalDate.of(1990, 1, 1);

        List<Author> authors = this.authorRepository.findDistinctByBooksReleaseDateBefore(beforeDate);

        authors.forEach(a -> System.out.printf("%s %s\n",
                a.getFirstName(),
                a.getLastName()));
    }

    private void getAllBooksAfter2000() {
        LocalDate dateAfter = LocalDate.of(2000, 12, 31);

        List<Book> books = this.bookRepository.findByReleaseDateAfter(dateAfter);

        int count = this.bookRepository.findByReleaseDateAfter(dateAfter).size();

        books.stream()
                .map(Book::getTitle)
                .forEach(System.out::println);

        System.out.printf("Total count : %d\n", count);
    }
}
