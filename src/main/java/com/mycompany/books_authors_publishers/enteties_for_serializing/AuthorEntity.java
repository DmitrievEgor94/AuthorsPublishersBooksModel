package com.mycompany.books_authors_publishers.enteties_for_serializing;

import com.mycompany.books_authors_publishers.Author;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AuthorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @OnlyInteger
    private Integer id;
    private String name;
    @Date
    private String dayOfBirthday;
    @Date
    @PermissionToBeNull
    private String dayOfDeath;
    private String sex;

    public AuthorEntity(Author author, int id) {
        final String ABSENT_DEATH_DATE = "-";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        this.name = author.getName();
        this.dayOfBirthday = author.getDayOfBirthday().format(formatter);

        LocalDate dayOfDeath = author.getDayOfDeath().orElse(null);
        if (dayOfDeath == null)
            this.dayOfDeath = ABSENT_DEATH_DATE;
        else this.dayOfDeath = dayOfDeath.format(formatter);

        this.sex = author.getSex().name();
        this.id = id;
    }

    public AuthorEntity() {
    }

    public int getId() {

        return id;
    }

    public String getSex() {
        return sex;
    }

    public String getDayOfDeath() {

        return dayOfDeath;
    }

    public String getDayOfBirthday() {

        return dayOfBirthday;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj.getClass() != this.getClass()) return false;

        AuthorEntity entity = (AuthorEntity) obj;

        if (!this.name.equals(entity.name)) {
            return false;
        }
        if (!this.id.equals(entity.id)) {
            return false;
        }
        if (!this.dayOfBirthday.equals(entity.dayOfBirthday)) {
            return false;
        }

        return this.dayOfDeath.equals(entity.dayOfDeath) && this.sex.equals(entity.sex);
    }
}
