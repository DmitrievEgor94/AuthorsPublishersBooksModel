import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AuthorsListCreator {

    static List<Author> getListWithAuthors(String fileNameWithAuthors) throws FileNotFoundException {
        Scanner scannerOfAuthors = new Scanner(new File(fileNameWithAuthors));
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

                if (sexInFile.equals("male")) sex = Author.Sex.male;
                else if (sexInFile.equals("female")) sex = Author.Sex.female;
                else throw new WrongSexInFile();

                listOfAuthors.add(new Author(name, dayOfBirthday, dayOfDeath, sex));

                currentLineInFile++;
            } catch (WrongSexInFile e) {
                System.out.println(e + fileNameWithAuthors + ". Line: " + currentLineInFile);
                currentLineInFile++;
            } catch (DateTimeParseException e) {
                System.out.println(e + ". Error in file " + fileNameWithAuthors + ". Line: " + currentLineInFile);
                currentLineInFile++;
            }
        }

        return listOfAuthors;
    }

    public static class WrongSexInFile extends Exception {
        @Override
        public String toString() {
            return "Wrong name of sex in file ";
        }
    }

}