import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AuthorsListCreator {

    static private String fileName = "authors.txt";

    static List<Author> getListWithAuthors() throws FileNotFoundException {

        String fullName = AuthorsListCreator.class.getProtectionDomain().getCodeSource().getLocation().getPath()
                + fileName;

        Scanner scannerOfAuthors = new Scanner(new File(fullName));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        List<Author> listOfAuthors = new ArrayList<>();
        int currentLineInFile = 1;

        while (scannerOfAuthors.hasNext()) {
            try {
                String name = scannerOfAuthors.next();

                LocalDate dayOfBirthday = LocalDate.parse(scannerOfAuthors.next(), formatter);

                LocalDate dayOfDeath;

                String deathDateInFile = scannerOfAuthors.next();
                if (deathDateInFile.equals("-"))
                    dayOfDeath = null;
                else
                    dayOfDeath = LocalDate.parse(deathDateInFile, formatter);

                String sexInFile = scannerOfAuthors.next();

                Author.Sex sex;

                sex = Author.Sex.valueOf(sexInFile.toUpperCase());

                listOfAuthors.add(new Author(name, dayOfBirthday, dayOfDeath, sex));

            } catch (DateTimeParseException e) {
                System.out.println(e + ". Error in file " + fullName + ". Line: " + currentLineInFile);
            }
            currentLineInFile++;
        }

        return listOfAuthors;
    }

}