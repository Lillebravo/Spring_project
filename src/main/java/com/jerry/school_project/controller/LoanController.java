package com.jerry.school_project.controller;

import com.jerry.school_project.dto.UserLoanDTO;
import com.jerry.school_project.dto.LoanDetailsDTO;
import com.jerry.school_project.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LoanController {
    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    /**
     * Get user loans
     * Example: GET localhost:8080/api/users/1/loans
     */
    @GetMapping("/users/{userId}/loans")
    public ResponseEntity<?> getUserLoans(@PathVariable Long userId) {
        try {
            List<UserLoanDTO> loans = loanService.getUserLoans(userId);

            if (loans.isEmpty()) {
                return ResponseEntity.status(404).body("No loans found for user with id:" + userId);
            }

            return ResponseEntity.status(200).body(loans);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error occurred while retrieving user loans");
        }
    }

    /**
     * Loan a book to a user
     * Example: POST localhost:8080/api/loans?userId=1&bookId=2
     */
    @PostMapping("/loans")
    public ResponseEntity<?> loanBook(@RequestParam Long userId, @RequestParam Long bookId) {
        try {
            LoanDetailsDTO createdLoan = loanService.loanBook(userId, bookId);
            return ResponseEntity.status(201).body(createdLoan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error occurred while creating the loan");
        }
    }

    /**
     * Return a loaned book
     * Example: PUT localhost:8080/api/loans/1/return
     */
    @PutMapping("/loans/{id}/return")
    public ResponseEntity<?> returnBook(@PathVariable Long id) {
        try {
            LoanDetailsDTO returnedLoan = loanService.returnBook(id);
            return ResponseEntity.status(200).body(returnedLoan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error occurred while returning the book");
        }
    }

    /**
     * Extend a loan period
     * Example: PUT localhost:8080/api/loans/1/extend
     */
    @PutMapping("/loans/{id}/extend")
    public ResponseEntity<?> extendLoan(@PathVariable Long id) {
        try {
            LoanDetailsDTO extendedLoan = loanService.extendLoan(id);
            return ResponseEntity.status(200).body(extendedLoan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error occurred while extending the loan");
        }
    }
}