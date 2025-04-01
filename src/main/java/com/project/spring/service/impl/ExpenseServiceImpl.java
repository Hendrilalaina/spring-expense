package com.project.spring.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.project.spring.dto.ExpenseDTO;
import com.project.spring.entity.ExpenseEntity;
import com.project.spring.exception.ResourceNotFoundException;
import com.project.spring.repository.ExpenseRepository;
import com.project.spring.service.ExpenseService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * Service implementation for Expense module
 */
@Service
public class ExpenseServiceImpl implements ExpenseService {
    
	private final ExpenseRepository expenseRepository;
	private final ModelMapper modelMapper;
	
    @Autowired
    public ExpenseServiceImpl (ExpenseRepository expenseRepository, ModelMapper modelMapper) {
    	this.expenseRepository = expenseRepository;
    	this.modelMapper = modelMapper;
    }

    /**
     * It will fetch the expenses from database
     * @return list
     */
    @Override
    public List<ExpenseDTO> getAllExpenses() {
        List<ExpenseEntity> list = expenseRepository.findAll();
        return list.stream()
                   .map(expense -> modelMapper.map(expense, ExpenseDTO.class))
                   .collect(Collectors.toList());
    }

    /**
     * It will fetch expense from database
     * @param expenseId
     * @return ExpenseDTO
     */
    @Override
    public ExpenseDTO getExpenseByExpenseId(String expenseId) {
    	ExpenseEntity optionalExpense = expenseRepository.findByExpenseId(expenseId)
    			.orElseThrow(() -> new ResourceNotFoundException("Expense not found for the expense id " + expenseId));
    	return modelMapper.map(optionalExpense, ExpenseDTO.class);
    }
//    @Override
//    public ExpenseDTO getExpenseById(Long id) {
//        ExpenseEntity expense = expenseRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Expense not found"));
//        return modelMapper.map(expense, ExpenseDTO.class);
//    }
//
//    @Override
//    public ExpenseDTO createExpense(ExpenseRequest request) {
//        ExpenseEntity expense = ExpenseEntity.builder()
//                .expenseId(UUID.randomUUID().toString())  // Generate a unique expenseId
//                .name(request.getName())
//                .note(request.getNote())
//                .category(request.getCategory())
//                .date(request.getDate())
//                .amount(request.getAmount())
//                .build();
//
//        ExpenseEntity savedExpense = expenseRepository.save(expense);
//        return modelMapper.map(savedExpense, ExpenseDTO.class);
//    }
//
//    @Override
//    public ExpenseDTO updateExpense(Long id, ExpenseRequest request) {
//        ExpenseEntity expense = expenseRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Expense not found"));
//
//        expense.setName(request.getName());
//        expense.setNote(request.getNote());
//        expense.setCategory(request.getCategory());
//        expense.setDate(request.getDate());
//        expense.setAmount(request.getAmount());
//
//        ExpenseEntity updatedExpense = expenseRepository.save(expense);
//        return modelMapper.map(updatedExpense, ExpenseDTO.class);
//    }
//
//    @Override
//    public void deleteExpense(Long id) {
//        ExpenseEntity expense = expenseRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Expense not found"));
//        expenseRepository.delete(expense);
//    }

}
