package com.mycompany.books_authors_publishers;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BooksListCreator {

    public static List<Book> getListWithBooks(List<Author> createdAuthors) throws FileNotFoundException {

        String fullNameFileBooks = BooksListCreator.class.getResource("books.txt").getPath();
        String fullNameFileBooksTheirAuthors = BooksListCreator.class.getResource("book-authors.txt").getPath();

        List<PairBookAuthor> pairBookAuthorList = new ArrayList<>();
        List<Book> books = new ArrayList<>();

        try (Scanner scannerOfBookAuthors = new Scanner(new File(fullNameFileBooksTheirAuthors));
             Scanner scannerOfBooks = new Scanner(new File(fullNameFileBooks))) {

            while (scannerOfBookAuthors.hasNext()) {
                String bookTitle = scannerOfBookAuthors.next();
                String author = scannerOfBookAuthors.next();
                pairBookAuthorList.add(new PairBookAuthor(bookTitle, author));
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            Map<String, List<String>> mapWithBookAuthors = pairBookAuthorList.stream()
                    .collect(Collectors.groupingBy(p -> p.bookTitle
                            , Collectors.mapping(p -> p.authorName, Collectors.toList())));

            while (scannerOfBooks.hasNext()) {

                String title = scannerOfBooks.next();
                LocalDate dayOfPublication = LocalDate.parse(scannerOfBooks.next(), formatter);
                List<String> authorsNameForCurrentBook = mapWithBookAuthors.get(title);

                List<Author> authorsObjectForCurrentBook = createdAuthors.stream()
                        .filter(x -> authorsNameForCurrentBook.contains(x.getName()))
                        .collect(Collectors.toList());

                books.add(new Book(title, dayOfPublication, authorsObjectForCurrentBook));
            }
        } catch (DateTimeParseException e) {
            System.out.println(e + ". Error in file " + fullNameFileBooks);
        }

        return books;
    }

    static class PairBookAuthor {

        private String bookTitle;
        private String authorName;

        public PairBookAuthor(String bookTitle, String authorName) {
            this.bookTitle = bookTitle;
            this.authorName = authorName;
        }

        public String getAuthorName() {
            return authorName;
        }
    }
}
