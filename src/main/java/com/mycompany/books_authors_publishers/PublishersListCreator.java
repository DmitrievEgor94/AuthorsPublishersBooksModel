package com.mycompany.books_authors_publishers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PublishersListCreator {

    public static List<Publisher> getListWithPublishers(List<Book> books) throws FileNotFoundException {

        String fileWithPublishers = PublishersListCreator.class.getResource("publishers.txt").getPath();
        String filePublisherWithBooks = PublishersListCreator.class.getResource("publisher-books.txt").getPath();

        try (Scanner publishersScanner = new Scanner(new File(fileWithPublishers));
             Scanner publisherBooksScanner = new Scanner(new File(filePublisherWithBooks))) {

            List<Publisher> publishers = new ArrayList<>();

            List<PublisherAndBook> publisherAndBooksArrayList = new ArrayList<>();
            while (publisherBooksScanner.hasNext()) {
                String publisherName = publisherBooksScanner.next();
                String bookName = publisherBooksScanner.next();
                publisherAndBooksArrayList.add(new PublisherAndBook(publisherName, bookName));
            }

            Map<String, List<String>> mapPublisherBooks = publisherAndBooksArrayList.stream()
                    .collect(Collectors.groupingBy(x -> x.publisherName
                            , Collectors.mapping(x -> x.bookName, Collectors.toList())));

            while (publishersScanner.hasNext()) {
                String publisherName = publishersScanner.next();

                List<String> publisherBooksNames = mapPublisherBooks.get(publisherName);

                List<Book> booksOfPublisher = books.stream()
                        .filter(b -> publisherBooksNames.contains(b.getTitle()))
                        .collect(Collectors.toList());

                publishers.add(new Publisher(publisherName, booksOfPublisher));
            }
            return publishers;
        }
    }

    static class PublisherAndBook {
        String publisherName;
        String bookName;

        public PublisherAndBook(String publisherName, String bookName) {
            this.publisherName = publisherName;
            this.bookName = bookName;
        }
    }
}
