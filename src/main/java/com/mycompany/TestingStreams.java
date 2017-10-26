package com.mycompany;

import com.mycompany.books_authors_publishers.Author;
import com.mycompany.books_authors_publishers.AuthorsListCreator;
import com.mycompany.books_authors_publishers.Book;
import com.mycompany.books_authors_publishers.BooksListCreator;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.YEARS;
import static java.util.AbstractMap.SimpleEntry;
import static java.util.Map.Entry;

public class TestingStreams {
    public static void testingStreams() {

        List<Author> authors;
        List<Book> books;

        try {
            authors = AuthorsListCreator.getListWithAuthors();
            books = BooksListCreator.getListWithBooks(authors);
        } catch (FileNotFoundException e) {
            System.out.println(e);
            return;
        }

        OptionalDouble averageAge = getAverageAgeOfAuthors(authors);
        System.out.println("Average age of authors: " + averageAge.orElse(0) + "\n");

        System.out.println("Sorted list of authors ordered by age:");
        System.out.println(getSortedListOfAuthorsByAge(authors));

        System.out.println("\n\u001B[31mPensioners:");
        System.out.println(getPensioners(authors));

        System.out.println("\n\u001B[35mBooks with ages:");
        System.out.println(getListOfBooksWithAges(books));

        System.out.println("\n\u001B[32mAuthors thar wrote with other authors:");
        System.out.println(getAuthorsWroteWithOthers(books));

        System.out.println("\n\u001B[30mAuthors and their books");
        System.out.println(getMapAuthorWithBooks(books));
    }

    public static OptionalDouble getAverageAgeOfAuthors(List<Author> authors) {
        LocalDate currentDay = LocalDate.now();

        return authors.stream()
                .mapToLong(x -> {
                    if (x.getDayOfDeath().isPresent()) {
                        return YEARS.between(x.getDayOfBirthday(), x.getDayOfDeath().get());
                    } else {
                        return YEARS.between(x.getDayOfBirthday(), currentDay);
                    }
                })
                .average();
    }

    public static List<Author> getSortedListOfAuthorsByAge(List<Author> authors) {
        LocalDate currentDay = LocalDate.now();

        return authors.stream()
                .sorted(Comparator.comparingLong(value -> value.getDayOfDeath().isPresent()
                        ? DAYS.between(value.getDayOfBirthday(), value.getDayOfDeath().get())
                        : DAYS.between(value.getDayOfBirthday(), currentDay)))
                .collect(Collectors.toList());

    }

    public static List<Author> getPensioners(List<Author> authors) {
        LocalDate currentDay = LocalDate.now();

        Predicate<Author> conditionsForPensioners = (x) -> {
            if (!x.getDayOfDeath().isPresent()) {
                if (YEARS.between(x.getDayOfBirthday(), currentDay) > 65 && x.getSex() == Author.Sex.MALE
                        || YEARS.between(x.getDayOfBirthday(), currentDay) > 63 && x.getSex() == Author.Sex.FEMALE)
                    return true;
            }
            return false;
        };

        return authors.stream()
                .filter(conditionsForPensioners)
                .collect(Collectors.toList());
    }

    public static List<Author> getAuthorsWroteWithOthers(List<Book> books) {
        return books.stream()
                .filter(b -> b.getAuthors().size() > 1)
                .map(Book::getAuthors)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public static List<BookTitleAge> getListOfBooksWithAges(List<Book> books) {
        LocalDate currentDay = LocalDate.now();

        return books.stream()
                .map((x) -> new BookTitleAge(x.getTitle(), YEARS.between(x.getPublicationDate(), currentDay)))
                .collect(Collectors.toList());
    }

    public static Map<Author, List<Book>> getMapAuthorWithBooks(List<Book> books) {
        return books.stream()
                .flatMap(book -> book.getAuthors().stream()
                        .map(author -> new SimpleEntry<>(author, book)))
                .collect(Collectors.groupingBy(Entry::getKey, Collectors.mapping(Entry::getValue, Collectors.toList())));
    }

    static private class BookTitleAge {
        String bookTitle;
        long age;

        public BookTitleAge(String bookTitle, long age) {
            this.bookTitle = bookTitle;
            this.age = age;
        }

        @Override
        public String toString() {
            return " " + bookTitle + " " + age;
        }
    }
}
