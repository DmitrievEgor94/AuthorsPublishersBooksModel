package com.mycompany;

import com.mycompany.books_authors_publishers.*;
import com.mycompany.books_authors_publishers.enteties_for_serializing.AuthorEntity;
import com.mycompany.books_authors_publishers.enteties_for_serializing.BookEntity;
import com.mycompany.books_authors_publishers.enteties_for_serializing.PublisherEntity;
import com.mycompany.books_authors_publishers.entities_creators_and_restorators.*;
import com.mycompany.serializer.JavaSerializer;
import com.mycompany.serializer.Serializer;
import com.mycompany.serializer.TextFormatSerializer;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SerializersTest {
    private List<Author> authors;
    private List<Book> books;
    private List<Publisher> publishers;

    private List<AuthorEntity> authorsEntities;
    private List<BookEntity> booksEntities;
    private List<PublisherEntity> publishersEntities;

    public SerializersTest() throws FileNotFoundException {
        authors = AuthorsListCreator.getListWithAuthors();
        books = BooksListCreator.getListWithBooks(authors);
        publishers = PublishersListCreator.getListWithPublishers(books);

        authorsEntities = AuthorEntitiesCreator.getListAuthorEntities(authors);
        booksEntities = BookEntitiesCreator.getListBookEntities(books, authorsEntities);
        publishersEntities = PublisherEntitiesCreator.getListPublisherEntities(publishers, booksEntities);
    }

    @Test
    public void testSerializers() throws Exception {
        Serializer textSerializer = new TextFormatSerializer();
        Serializer javaSerializer = new JavaSerializer();

        String rootFolder = Serializer.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        String authorsTextSerializerFile = rootFolder + "/authorsTextFormat.txt";
        String booksTextSerializerFile = rootFolder + "/booksTextFormat.txt";
        String publishersTextSerializerFile = rootFolder + "/publishersTextFormat.txt";

        String authorsJavaSerializerFile = rootFolder + "/authorsHexFormat.txt";
        String booksJavaSerializerFile = rootFolder + "/booksHexFormat.txt";
        String publishersJavaSerializerFile = rootFolder + "/publishersHexFormat.txt";

        useSerializer(textSerializer, authorsTextSerializerFile, booksTextSerializerFile,publishersTextSerializerFile);
        useSerializer(javaSerializer, authorsJavaSerializerFile,booksJavaSerializerFile,publishersJavaSerializerFile);
    }

    private void useSerializer(Serializer serializer, String authorsFile, String booksFile, String publishersFile) throws Exception {

        serializer.serializeObject(authorsEntities, authorsFile);
        assertEquals(authorsEntities, serializer.deserializeObject(authorsFile, AuthorEntity.class));

        serializer.serializeObject(booksEntities, booksFile);
        assertEquals(booksEntities, serializer.deserializeObject(booksFile, BookEntity.class));

        serializer.serializeObject(publishersEntities, publishersFile);
        assertEquals(publishersEntities, serializer.deserializeObject(publishersFile, PublisherEntity.class));
    }

    @Test
    public void getAuthorsTest() {
        assertEquals(authors, AuthorsRestorator.getListOfAuthors(authorsEntities));
    }

    @Test
    public void getBooksTest() {
        assertEquals(books, BooksRestorator.getListOfBooks(booksEntities, authors, authorsEntities));
    }

    @Test
    public void getPublishersTest() {
        assertEquals(publishers, PublishersRestorator.getListOfPublishers(publishersEntities, booksEntities, books));
    }


}