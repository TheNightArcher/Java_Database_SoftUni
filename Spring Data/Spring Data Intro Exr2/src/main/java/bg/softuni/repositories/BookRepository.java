package bg.softuni.repositories;

import bg.softuni.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
    List<Book> findByReleaseDateAfter(LocalDate date);

    Set<Book> findByAuthorId(int authorId);
}
