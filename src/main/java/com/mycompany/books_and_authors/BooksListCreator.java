package com.mycompany.books_and_authors;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BooksListCreator {

    static private String fileNameBooks = "books.txt";
    static private String fileNameBooksTheirAuthors = "book-authors.txt";

    public static List<Book> getListOfBooks(List<Author> createdAuthors) throws FileNotFoundException {

        String fullNameFileBooks = BooksListCreator.class.getResource(fileNameBooks).getPath();
        String fullNameFileBooksTheireAuthors = BooksListCreator.class.getResource(fileNameBooksTheirAuthors).getPath();

        List<PairBookAuthor> pairBookAuthorList = new ArrayList<>();
        List<Book> books = new ArrayList<>();

        Scanner scannerOfBookAuthors = new Scanner(new File(fullNameFileBooksTheireAuthors));

        while (scannerOfBookAuthors.hasNext()) {
            String bookTitle = Optional.of(scannerOfBookAuthors.next()).orElse("");
            String author = Optional.of(scannerOfBookAuthors.next()).orElse("");

            pairBookAuthorList.add(new PairBookAuthor(bookTitle, author));
        }

        Scanner scannerOfBooks = new Scanner(new File(fullNameFileBooks));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        int currentLineInFile = 1;

        while (scannerOfBooks.hasNext()) {
            try {
                String title = scannerOfBooks.next();

                LocalDate dayOfPublication = LocalDate.parse(scannerOfBooks.next(), formatter);

                List<String> authorsNameForCurrentBook = pairBookAuthorList.stream()
                        .filter(x -> x.bookTitle.equals(title))
                        .map(PairBookAuthor::getAuthorName)
                        .collect(Collectors.toList());

                List<Author> authorsObjectForCurrentBook = createdAuthors.stream()
                        .filter(x -> authorsNameForCurrentBook.contains(x.getName()))
                        .collect(Collectors.toList());


                books.add(new Book(title, dayOfPublication, authorsObjectForCurrentBook));

            } catch (DateTimeParseException e) {
                System.out.println(e + ". Error in file " + fullNameFileBooks + ". Line: " + currentLineInFile);
            }
            currentLineInFile++;
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

        public String getBookTitle() {
            return bookTitle;
        }

        public String getAuthorName() {
            return authorName;
        }
    }
}
