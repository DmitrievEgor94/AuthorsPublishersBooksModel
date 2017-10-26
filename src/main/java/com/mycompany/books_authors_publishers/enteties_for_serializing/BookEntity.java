package com.mycompany.books_authors_publishers.enteties_for_serializing;

import com.mycompany.books_authors_publishers.Book;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @OnlyInteger
    private Integer id;
    private String title;
    @Date
    private String publicationDate;
    @ListField
    private List<Integer> authorsId;

    public BookEntity() {
    }

    public BookEntity(Book book, Map<String, Integer> mapWithAuthorsNames, int id) {
        this.title = book.getTitle();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.publicationDate = book.getPublicationDate().format(formatter);

        this.authorsId = book.getAuthors().stream()
                .map(author -> mapWithAuthorsNames.get(author.getName()))
                .collect(Collectors.toList());
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public List<Integer> getAuthorsId() {
        return authorsId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (this.getClass() != obj.getClass()) return false;

        BookEntity entity = (BookEntity) obj;

        if (!this.id.equals(entity.id)) {
            return false;
        }

        if (!this.title.equals(entity.title)) {
            return false;
        }

        if (!this.publicationDate.equals(entity.publicationDate)) {
            return false;
        }

        if (!this.authorsId.equals(entity.authorsId)) {
            return false;
        }

        return true;
    }
}
