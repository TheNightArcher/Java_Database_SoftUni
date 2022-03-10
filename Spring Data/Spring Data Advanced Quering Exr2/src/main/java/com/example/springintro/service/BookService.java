package com.example.springintro.service;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.EditionType;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName);

    void findAllBooksByAgeRestriction(AgeRestriction type);

    void findAllBooksByEditionTypeAndLessThenGivenCopies(EditionType type, int copies);

    void findAllBooksPriceBiggerOrLowerThenGiven(BigDecimal lower, BigDecimal higher);

    void findAllBooksNotInGivenYear(int year);

    void findAllBooksBeforeGivenDate(String date);

    void findAllTitlesContainsGivenLetters(String letters);

    void findAllAuthorsWithLastNameLike(String letters);

    int findAllTitlesBiggerThenGivenLength(int length);

    void findGivenTitle(String title);
}
