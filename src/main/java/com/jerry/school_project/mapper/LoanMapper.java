package com.jerry.school_project.mapper;

import com.jerry.school_project.dto.UserLoanDTO;
import com.jerry.school_project.dto.LoanDetailsDTO;
import com.jerry.school_project.entity.Loan;
import com.jerry.school_project.entity.Book;
import com.jerry.school_project.entity.Author;
import com.jerry.school_project.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoanMapper {

    // Convert Loan to UserLoanDTO (for displaying user's loans)
    public UserLoanDTO toUserLoanDTO(Loan loan, Book book, Author author) {
        if (loan == null || book == null || author == null) {
            return null;
        }

        return new UserLoanDTO(
                loan.getId(),
                book.getId(),
                book.getTitle(),
                author.getFullName(),
                loan.getBorrowedDate(),
                loan.getDueDate(),
                loan.getReturnedDate()
        );
    }

    // Convert Loan to LoanDetailsDTO (for displaying complete loan information)
    public LoanDetailsDTO toLoanDetailsDTO(Loan loan, User user, Book book, Author author) {
        if (loan == null || user == null || book == null || author == null) {
            return null;
        }

        return new LoanDetailsDTO(
                loan.getId(),
                user.getId(),
                user.getFullName(),
                book.getId(),
                book.getTitle(),
                author.getFullName(),
                loan.getBorrowedDate(),
                loan.getDueDate(),
                loan.getReturnedDate()
        );
    }

    // Convert list of loans to UserLoanDTO list
    public List<UserLoanDTO> toUserLoanDTOList(List<Loan> loans, List<Book> books, List<Author> authors) {
        if (loans == null || loans.isEmpty()) {
            return List.of();
        }

        return loans.stream()
                .map(loan -> {
                    Book book = books.stream()
                            .filter(b -> b.getId().equals(loan.getBookId()))
                            .findFirst()
                            .orElse(null);

                    if (book == null) return null;

                    Author author = authors.stream()
                            .filter(a -> a.getId().equals(book.getAuthorId()))
                            .findFirst()
                            .orElse(null);

                    if (author == null) return null;

                    return toUserLoanDTO(loan, book, author);
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }
}