package com.project.spring.repository;

import com.project.spring.entity.ExpenseEntity;

import java.util.List;
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
	
	/**
	 * It will find all expenses of the owner id
	 * @param id
	 * @return ExpenseEntity
	 */
	List<ExpenseEntity> findByOwnerId(Long id);
	
	/**
	 * It will find the single expense of the owner id from database
	 * @param id
	 * @param expenseId
	 * @return
	 */
	Optional<ExpenseEntity> findByOwnerIdAndExpenseId(Long id, String expenseId);
}
