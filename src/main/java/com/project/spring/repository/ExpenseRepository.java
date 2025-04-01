package com.project.spring.repository;

import com.project.spring.entity.ExpenseEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for Expense resource
 */
@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
	
	/**
	 * It will find the single expense from database
	 * @param expenseId
	 * @return ExpenseEntity
	 */
	Optional<ExpenseEntity> findByExpenseId(String expenseId);
}
