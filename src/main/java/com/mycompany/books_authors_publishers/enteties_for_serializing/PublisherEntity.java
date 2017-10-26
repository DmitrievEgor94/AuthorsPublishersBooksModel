package com.mycompany.books_authors_publishers.enteties_for_serializing;

import com.mycompany.books_authors_publishers.Publisher;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PublisherEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    @ListField
    private List<Integer> books;

    public PublisherEntity() {
    }


    public PublisherEntity(Publisher publisher, Map<String, Integer> mapWithBooksTitles, int id) {
        this.name = publisher.getName();
        this.books = publisher.getBooks().stream()
                .map(b -> mapWithBooksTitles.get(b.getTitle()))
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public List<Integer> getBooksId() {
        return books;
    }

    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (this.getClass() != obj.getClass()) {
            return false;
        }

        PublisherEntity entity = (PublisherEntity) obj;

        if (!this.name.equals(entity.name)) {
            return false;
        }

        if (!this.books.equals(entity.books)) {
            return false;
        }

        return true;
    }
}
