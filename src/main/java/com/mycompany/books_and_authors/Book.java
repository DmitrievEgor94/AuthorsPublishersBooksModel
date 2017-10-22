package com.mycompany.books_and_authors;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class Book {

    private String title;
    private LocalDate publicationDate;
    private List<Author> authors;

    public Book(String title, LocalDate publicationDate, List<Author> authors) {
        this.title = title;
        this.publicationDate = publicationDate;
        this.authors = authors;
    }

    @Override
    public String toString() {
        return title;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public List<Author> getAuthors() {
        return Collections.unmodifiableList(authors);
    }
}
