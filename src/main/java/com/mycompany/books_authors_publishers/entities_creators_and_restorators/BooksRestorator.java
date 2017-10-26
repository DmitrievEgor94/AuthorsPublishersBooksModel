package com.mycompany.books_authors_publishers.entities_creators_and_restorators;

import com.mycompany.books_authors_publishers.Author;
import com.mycompany.books_authors_publishers.Book;
import com.mycompany.books_authors_publishers.enteties_for_serializing.AuthorEntity;
import com.mycompany.books_authors_publishers.enteties_for_serializing.BookEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BooksRestorator {

    static public List<Book> getListOfBooks(List<BookEntity> entities, List<Author> authors, List<AuthorEntity> authorEntities) {
        return entities.stream()
                .map(b->getBook(b,authors,authorEntities))
                        .collect(Collectors.toList());
    }

    static private Book getBook(BookEntity bookEntity, List<Author> authors, List<AuthorEntity> authorEntities) {

        Map<String, Author> mapNameAndAuthor = authors.stream()
                .collect(Collectors.toMap(Author::getName, a -> a));

        Map<Integer, Author> mapIdAuthor = authorEntities.stream()
                .collect(Collectors.toMap(AuthorEntity::getId, a -> mapNameAndAuthor.get(a.getName())));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate dayOfPublication = LocalDate.parse(bookEntity.getPublicationDate(), formatter);

        return new Book(bookEntity.getTitle(), dayOfPublication
                , bookEntity.getAuthorsId().stream()
                .map(mapIdAuthor::get)
                .collect(Collectors.toList()));
    }
}
