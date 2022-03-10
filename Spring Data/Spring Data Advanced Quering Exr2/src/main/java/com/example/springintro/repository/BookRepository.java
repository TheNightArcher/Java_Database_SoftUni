package com.example.springintro.repository;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.EditionType;
import com.example.springintro.model.entity.SummaryBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByReleaseDateAfter(LocalDate releaseDateAfter);

    List<Book> findAllByReleaseDateBefore(LocalDate releaseDateBefore);

    List<Book> findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(String author_firstName, String author_lastName);

    List<Book> findByAgeRestriction(AgeRestriction type);

    List<Book> findByEditionTypeAndCopiesLessThan(EditionType type, int copies);

    List<Book> findByPriceLessThanOrPriceGreaterThan(BigDecimal lower, BigDecimal higher);

    List<Book> findByReleaseDateBeforeOrReleaseDateAfter(LocalDate before, LocalDate after);

    List<Book> findByReleaseDateBefore(LocalDate date);

    List<Book> findByTitleContaining(String letters);

    List<Book> findByAuthorLastNameStartingWith(String letters);

    @Query("SELECT COUNT (b) AS count FROM Book AS b " +
            "WHERE length(b.title) > :length")
    int findByTitleGreaterThanGivenLength(int length);


    @Query("SELECT b.title AS title, b.editionType AS editionType, b.ageRestriction AS ageRestriction," +
            "b.price AS price FROM Book AS b " +
            "WHERE b.title = :title")
    List<SummaryBook> findInformationAboutGivenBook(String title);
}
