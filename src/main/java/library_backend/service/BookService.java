package library_backend.service;

import library_backend.model.Book;
import library_backend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Book not found: " + id));
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        Book book = getBookById(id);
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setGenre(bookDetails.getGenre());
        book.setIsbn(bookDetails.getIsbn());
        book.setYear(bookDetails.getYear());
        book.setQuantity(bookDetails.getQuantity());
        book.setStatus(bookDetails.getStatus());
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.delete(getBookById(id));
    }
}
