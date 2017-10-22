package com.mycompany.books_and_authors;

import java.time.LocalDate;
import java.util.Optional;

public class Author {

    private String name;
    private LocalDate dayOfBirthday;
    private LocalDate dayOfDeath;
    private Sex sex;

    public enum Sex {MALE, FEMALE}

    public Author(String name, LocalDate dayOfBirthday, LocalDate dayOfDeath, Sex sex) {
        this.name = name;
        this.dayOfBirthday = dayOfBirthday;
        this.dayOfDeath = dayOfDeath;
        this.sex = sex;
    }

    public LocalDate getDayOfBirthday() {
        return dayOfBirthday;
    }

    public Optional<LocalDate> getDayOfDeath() {
        return Optional.ofNullable(dayOfDeath);
    }

    @Override
    public String toString() {
        return name;
    }

    public Sex getSex() {
        return sex;
    }

    public String getName() {
        return name;
    }
}