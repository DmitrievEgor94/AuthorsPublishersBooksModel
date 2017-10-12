import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.YEARS;

public class TestingStreams {

    public static void printList(List<?> list) {
        list.stream().forEach(System.out::println);
    }

    public static void testingStreams() {
        try {
            String nameOfPackage = "src\\main\\resources\\";
            LocalDate currentDay = LocalDate.now();

            List<Author> authors = AuthorsListCreator.getListWithAuthors(nameOfPackage + "authors.txt");
            List<Book> books = BooksListCreator.getListOfBooks(nameOfPackage + "books.txt",
                    nameOfPackage + "book-authors.txt", authors);

            OptionalDouble averageAge = authors.stream()
                    .mapToLong(x -> YEARS.between(x.getDayOfBirthday(), currentDay))
                    .average();

            System.out.println("Average age of authors: " + averageAge.orElse(0) + "\n");

            List<Author> sortedListOfAuthors = authors.stream()
                    .sorted((x, y) -> (int) YEARS.between(y.getDayOfBirthday(), x.getDayOfBirthday()))
                    .collect(Collectors.toList());

            System.out.println("Sorted list of authors orders by age:");
            printList(sortedListOfAuthors);

            Predicate<Author> conditionsForPensioners = (x) -> {
                if (!x.getDayOfDeath().isPresent()) {
                    if (YEARS.between(x.getDayOfBirthday(), currentDay) > 65 && x.getSex() == Author.Sex.male)
                        return true;

                    if (YEARS.between(x.getDayOfBirthday(), currentDay) > 63 && x.getSex() == Author.Sex.female)
                        return true;
                }

                return false;
            };

            List<Author> pensioners = authors.stream()
                    .filter(conditionsForPensioners)
                    .collect(Collectors.toList());

            System.out.println("\u001B[31mPensioners:");
            printList(pensioners);

            class BookTitleAge {
                String bookTitle;
                long age;

                public BookTitleAge(String bookTitle, long age) {
                    this.bookTitle = bookTitle;
                    this.age = age;
                }

                @Override
                public String toString() {
                    return " " + bookTitle + " " + age;
                }
            }

            System.out.println("\u001B[35mBooks with ages:");
            List<BookTitleAge> pairBookAgeList = books.stream()
                    .map((x) -> new BookTitleAge(x.getTitle(), YEARS.between(x.getPublicationDate(), currentDay)))
                    .collect(Collectors.toList());
            printList(pairBookAgeList);

            System.out.println("\n\u001B[32mAuthors thar wrote with other authors:");
            List<Author> authorsWroteWithOthers = books.stream()
                    .map(x -> {
                        if (x.getAuthors().size() > 1)
                            return x.getAuthors();
                        else return new ArrayList<Author>();
                    }).flatMap(l -> l.stream())
                    .collect(Collectors.toSet())
                    .stream().collect(Collectors.toList());


            printList(authorsWroteWithOthers);
            class AuthorBooks {
                String author;
                List<String> titleOfBooks;

                public AuthorBooks(String author, List<String> titleOfBooks) {
                    this.author = author;
                    this.titleOfBooks = titleOfBooks;
                }

                @Override
                public String toString() {
                    String out = author;

                    for (String titleOfBook : titleOfBooks) {
                        out += "\n   " + titleOfBook;
                    }

                    return out;
                }
            }

            List<AuthorBooks> authorsAndTheirBooks = authors.stream()
                    .map(x -> {
                        List<String> booksOfAuthor = books.stream().
                                filter((y) -> y.getAuthors().contains(x)).
                                map(y -> y.getTitle()).
                                collect(Collectors.toList());
                        return new AuthorBooks(x.getName(), booksOfAuthor);
                    }).collect(Collectors.toList());

            System.out.println("\n\u001B[30mAuthors and theire books");
            printList(authorsAndTheirBooks);

        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

    }

    public static void main(String[] args) {
        testingStreams();
    }
}
