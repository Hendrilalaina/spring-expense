package com.project.spring.service;

import java.util.List;

import com.project.spring.dto.ExpenseDTO;

/*
 * Service interface for Expense
 */
public interface ExpenseService {
	/**
	 * It will fetch the expenses from database
	 * @return list
	 */
    List<ExpenseDTO> getAllExpenses();
    
    /**
     * It will fetch the expense details from database
     * @param expenseId
     * @return ExpenseDTO
     */
    ExpenseDTO getExpenseByExpenseId(String expenseId);
}
