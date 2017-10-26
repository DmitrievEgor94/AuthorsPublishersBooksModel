package com.mycompany.books_authors_publishers.entities_creators_and_restorators;

import com.mycompany.books_authors_publishers.Book;
import com.mycompany.books_authors_publishers.enteties_for_serializing.AuthorEntity;
import com.mycompany.books_authors_publishers.enteties_for_serializing.BookEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BookEntitiesCreator {
    static public List<BookEntity> getListBookEntities(List<Book> books, List<AuthorEntity> authorEntities) {

        Map<String, Integer> mapAuthorNameId = authorEntities.stream()
                .collect(Collectors.toMap(AuthorEntity::getName, AuthorEntity::getId));

        AtomicInteger counter = new AtomicInteger(0);

        return books.stream()
                .map(b -> new BookEntity(b, mapAuthorNameId, counter.incrementAndGet()))
                .collect(Collectors.toList());
    }
}
