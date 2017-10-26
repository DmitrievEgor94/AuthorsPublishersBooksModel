package com.mycompany.books_authors_publishers;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AuthorsListCreator {

    static private final String ABSENT_DEATH_DATE = "-";

    static public List<Author> getListWithAuthors() throws FileNotFoundException {

        String fullNameOfFileWithAuthors = AuthorsListCreator.class.getResource("authors.txt").getPath();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        List<Author> listOfAuthors = new ArrayList<>();

        try (Scanner scannerOfAuthors = new Scanner(new File(fullNameOfFileWithAuthors))) {
            while (scannerOfAuthors.hasNext()) {
                String name = scannerOfAuthors.next();

                LocalDate dayOfBirthday = LocalDate.parse(scannerOfAuthors.next(), formatter);

                LocalDate dayOfDeath;

                String deathDateInFile = scannerOfAuthors.next();
                if (deathDateInFile.equals(ABSENT_DEATH_DATE))
                    dayOfDeath = null;
                else
                    dayOfDeath = LocalDate.parse(deathDateInFile, formatter);

                String sexInFile = scannerOfAuthors.next();

                Author.Sex sex = Author.Sex.valueOf(sexInFile.toUpperCase());

                listOfAuthors.add(new Author(name, dayOfBirthday, dayOfDeath, sex));

            }
        } catch (DateTimeParseException e) {
            System.out.println(e + ". Error in file " + fullNameOfFileWithAuthors);
        }
        return listOfAuthors;
    }

}