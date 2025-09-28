package controllers;

import classes.Book;
import classes.LibraryStoreManager;
import classes.CurrentDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201", "http://127.0.0.1:4200", "http://127.0.0.1:4201"})
public class BookController {
    
    private final LibraryStoreManager libraryManager;
    
    public BookController() {
        this.libraryManager = new LibraryStoreManager(new CurrentDate());
    }
    
    @GetMapping
    public ResponseEntity<?> getAllBooks() {
        try {
            java.util.List<Book> books = libraryManager.getAllBooks();
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"error\": \"Internal server error\"}");
        }
    }
    
    @GetMapping("/{isbn}")
    public ResponseEntity<?> getBook(@PathVariable long isbn) {
        try {
            Optional<Book> book = libraryManager.getBookByISBN(isbn);
            if (book.isPresent()) {
                return ResponseEntity.ok(book.get());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"error\": \"Internal server error\"}");
        }
    }
    
    @PutMapping("/{isbn}/copies")
    public ResponseEntity<?> updateBookCopies(@PathVariable long isbn, @RequestParam int copies) {
        try {
            libraryManager.UpdateBookCopies(isbn, copies);
            return ResponseEntity.ok().body("{\"message\": \"Book copies updated successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"error\": \"Internal server error\"}");
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(@RequestParam(required = false) String title) {
        try {
            if (title == null || title.trim().isEmpty()) {
                // If no title provided, return all books
                java.util.List<Book> books = libraryManager.getAllBooks();
                return ResponseEntity.ok(books);
            } else {
                // Search by title
                java.util.List<Book> books = libraryManager.searchBooksByTitle(title.trim());
                return ResponseEntity.ok(books);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"error\": \"Internal server error\"}");
        }
    }

    @GetMapping("/floor/{floorLevel}")
    public ResponseEntity<?> getBooksByFloor(@PathVariable int floorLevel) {
        try {
            java.util.List<Book> books = libraryManager.getBooksByFloor(floorLevel);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"error\": \"Internal server error\"}");
        }
    }
}