package com.mycompany.books_authors_publishers;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj.getClass() != this.getClass()) return false;

        Author author = (Author) obj;

        if (!this.name.equals(author.name)) {
            return false;
        }

        if (!this.dayOfBirthday.equals(author.dayOfBirthday)) {
            return false;
        }

        if (this.dayOfDeath == null) {
            if (author.dayOfDeath != null) {
                return false;
            }
        } else {
            if (!this.dayOfDeath.equals(author.dayOfDeath)) {
                return false;
            }
        }

        if (!this.sex.equals(author.sex)) {
            return false;
        }

        return true;
    }
}
