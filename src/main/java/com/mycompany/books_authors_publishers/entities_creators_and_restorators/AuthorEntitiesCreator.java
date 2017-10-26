package com.mycompany.books_authors_publishers.entities_creators_and_restorators;

import com.mycompany.books_authors_publishers.Author;
import com.mycompany.books_authors_publishers.enteties_for_serializing.AuthorEntity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AuthorEntitiesCreator {
    static public List<AuthorEntity> getListAuthorEntities(List<Author> authors) {
        AtomicInteger counter = new AtomicInteger(0);

        return authors.stream()
                .map(author -> new AuthorEntity(author,counter.incrementAndGet()))
                .collect(Collectors.toList());
    }
}
