package com.mycompany.books_authors_publishers.entities_creators_and_restorators;

import com.mycompany.books_authors_publishers.Book;
import com.mycompany.books_authors_publishers.Publisher;
import com.mycompany.books_authors_publishers.enteties_for_serializing.BookEntity;
import com.mycompany.books_authors_publishers.enteties_for_serializing.PublisherEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PublishersRestorator {

    static public List<Publisher> getListOfPublishers(List<PublisherEntity> entities, List<BookEntity> bookEntities, List<Book> books) {
        return entities.stream()
                .map(e -> getPublisher(e, bookEntities, books))
                .collect(Collectors.toList());
    }

    static private Publisher getPublisher(PublisherEntity entity, List<BookEntity> bookEntities, List<Book> books) {

        Map<String, Book> mapTitleBook = books.stream()
                .collect(Collectors.toMap(Book::getTitle, b -> b));

        Map<Integer, Book> mapIdBook = bookEntities.stream()
                .collect(Collectors.toMap(BookEntity::getId, b -> mapTitleBook.get(b.getTitle())));

        return new Publisher(entity.getName(),
                entity.getBooksId().stream()
                        .map(mapIdBook::get)
                        .collect(Collectors.toList()));
    }
}
