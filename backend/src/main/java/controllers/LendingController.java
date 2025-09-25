package controllers;

import classes.LibraryStoreManager;
import classes.CurrentDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/lending")
@CrossOrigin(origins = "http://localhost:4200")
public class LendingController {
    
    private final LibraryStoreManager libraryManager;
    
    public LendingController() {
        this.libraryManager = new LibraryStoreManager(new CurrentDate());
    }
    
    @PostMapping("/lend")
    public ResponseEntity<?> lendBook(@RequestParam int memberId, @RequestParam long isbn) {
        try {
            libraryManager.lendBook(memberId, isbn);
            return ResponseEntity.ok().body("{\"message\": \"Book lent successfully\"}");
        } catch (SQLException | IOException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"error\": \"Internal server error\"}");
        }
    }
    
    @PostMapping("/return")
    public ResponseEntity<?> returnBook(@RequestParam int memberId, @RequestParam long isbn) {
        try {
            libraryManager.returnItem(memberId, isbn);
            return ResponseEntity.ok().body("{\"message\": \"Book returned successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"error\": \"Internal server error\"}");
        }
    }
    
    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getMemberLendings(@PathVariable int memberId) {
        try {
            // You can extend this to return actual lending information
            return ResponseEntity.ok().body("{\"message\": \"Member lending info - extend as needed\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"error\": \"Internal server error\"}");
        }
    }
}