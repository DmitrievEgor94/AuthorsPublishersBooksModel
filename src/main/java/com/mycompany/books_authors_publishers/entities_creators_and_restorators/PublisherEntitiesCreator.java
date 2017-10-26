package com.mycompany.books_authors_publishers.entities_creators_and_restorators;

import com.mycompany.books_authors_publishers.Publisher;
import com.mycompany.books_authors_publishers.enteties_for_serializing.BookEntity;
import com.mycompany.books_authors_publishers.enteties_for_serializing.PublisherEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PublisherEntitiesCreator {
    static public List<PublisherEntity> getListPublisherEntities(List<Publisher> publishers
            , List<BookEntity> bookEntities) {
        AtomicInteger counter = new AtomicInteger(0);

        Map<String, Integer> mapBooksTitleId = bookEntities.stream()
                .collect(Collectors.toMap(BookEntity::getTitle, BookEntity::getId));

        return publishers.stream()
                .map(p -> new PublisherEntity(p, mapBooksTitleId, counter.incrementAndGet()))
                .collect(Collectors.toList());
    }
}
