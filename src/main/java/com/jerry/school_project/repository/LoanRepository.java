package com.jerry.school_project.repository;

import com.jerry.school_project.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    // Search loans a user has by their id
    List<Loan> findByUserId(Long userId);
}