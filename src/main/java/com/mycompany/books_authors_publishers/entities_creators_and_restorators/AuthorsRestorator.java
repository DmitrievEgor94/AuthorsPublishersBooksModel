package com.mycompany.books_authors_publishers.entities_creators_and_restorators;

import com.mycompany.books_authors_publishers.Author;
import com.mycompany.books_authors_publishers.enteties_for_serializing.AuthorEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorsRestorator {

    static private final String ABSENT_DEATH_DATE = "-";

    static public List<Author> getListOfAuthors(List<AuthorEntity> entities) {
        return entities.stream()
                .map(AuthorsRestorator::getAuthor)
                .collect(Collectors.toList());
    }

    static private Author getAuthor(AuthorEntity entity) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate dayOfBirthday = LocalDate.parse(entity.getDayOfBirthday(), formatter);

        LocalDate dayOfDeath = null;
        String dayOfDeathString = entity.getDayOfDeath();

        if (!dayOfDeathString.equals(ABSENT_DEATH_DATE)) {
            dayOfDeath = LocalDate.parse(dayOfDeathString, formatter);
        }

        Author.Sex sex = Author.Sex.valueOf(entity.getSex());

        return new Author(entity.getName(), dayOfBirthday, dayOfDeath, sex);
    }
}
