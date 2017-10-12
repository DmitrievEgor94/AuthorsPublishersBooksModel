import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class Book {

    private String title;
    private LocalDate publicationDate;
    private List<Author> authors;

    public Book(String title, LocalDate publicationDate, List<Author> authors) {
        this.title = title;
        this.publicationDate = publicationDate;
        this.authors = authors;

    }

    @Override
    public String toString() {
        String outString = title + "\n  PublicationDate:" + publicationDate + "\n  Authors:";

        for (Author author : authors) {
            outString += "\n   " + author.getName();
        }

        return outString + "\n";
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public List<Author> getAuthors() {
        return Collections.unmodifiableList(authors);
    }
}
