package nl.fontys.sebivenlo.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * DefaultLibrary is the central store of books.
 *
 * The inventory can be used to retrieve several views on the books within the
 * library. Some rudimentary search operations are supplied as well.
 *
 * The library is implemented as a singleton.
 *
 * @author Jan Trienes
 */
public class DefaultLibrary implements LibraryModel {

    /**
     * The only instance of this library.
     */
    private final List<Book> books;

    /**
     * Dummy book returned if no book is found.
     */
    public static final Book NULL_OBJECT_BOOK
            = new Book( 0, "Null object", "", "", "", Book.Language.ENGLISH, -1 );

    /**
     * The constructor loads the library catalogue file.
     *
     * @param books to add in this constructor.
     */
    public DefaultLibrary( List<Book> books ) {
        this.books = books;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<Book> getBooks() {
        return Collections.unmodifiableList(this.books);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Book> booksMatchSearchTerm( String searchTerm ) {
        return books.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(searchTerm.toLowerCase())
                        || book.getTitle().toLowerCase().contains(searchTerm.toLowerCase())
                        || book.getPublisher().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> authorsMatchSearchTerm( String searchTerm ) {
        return books.stream()
                .map(Book::getAuthor)
                .filter(author -> author.toLowerCase().contains(searchTerm.toLowerCase()))
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Book getBookById( long id ) {
        Book result = this.books.stream()
                .filter(book -> book.getId() == id).findAny().orElse(null);
        if (result == null) {
            return NULL_OBJECT_BOOK;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Book> booksMatchPredicate(
            Predicate<? super Book> searchPredicate ) {
        return this.books.stream().filter(searchPredicate).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for ( Book book : books ) {
            sb.append( book.toString() );
            sb.append( "\n" );
        }

        return sb.toString();
    }
}
