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
    
    /**
     * It will delete the expense from database
     * @param expenseId
     * @return void
     */
    void deleteExpenseByExpenseId(String id);
    
    /**
     * It will save the expense details to database
     * @param expenseDTO
     * @return ExpenseDTO
     */
    ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO);
    
    /**
     * It will update the expense details to database
     * @param expenseDTO
     * @param expnseId
     * return ExpenseDTO
     */
    ExpenseDTO updateExpenseDetails(ExpenseDTO expenseDTO, String expenseId);
}
