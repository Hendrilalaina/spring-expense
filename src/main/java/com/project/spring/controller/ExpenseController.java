package com.project.spring.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.spring.dto.ExpenseDTO;
import com.project.spring.io.ExpenseResponse;
import com.project.spring.service.ExpenseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin("*")
@Slf4j
@Tag(name = "Expense Constroller", description = "Expense Controller")
@RequestMapping(path = "/api/v1")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final ModelMapper modelMapper;
    
    public ExpenseController(ExpenseService expenseService, ModelMapper modelMapper) {
    	this.expenseService = expenseService;
    	this.modelMapper = modelMapper;
    }
    
    /**
     * It will fetch the expenses from service
     * @return list
     */
    @Operation(summary = "Get all expenses", 
    		   description = "Returns all expenses")
    @GetMapping("/expenses")
    public List<ExpenseResponse> getExpenses() {
    	log.info("API GET /expenses is called");
    	List<ExpenseDTO> list = expenseService.getAllExpenses();
    	log.info("Printing the data from service {}", list);
    	return list.stream()
    			.map(expense -> modelMapper.map(expense, ExpenseResponse.class))
    			.collect(Collectors.toList());
    }
    
    /**
     * It will fetch the single expense from service
     * @param expenseId
     * @return ExpenseResponse
     */
    @Operation(summary = "Get an expense by expenseId",
    		   description = "Returns an expense")
    @GetMapping("/expenses/{expenseId}")
    public ExpenseResponse getExpenseById(@PathVariable String expenseId) {
    	log.info("API GET /expenses/{} is called", expenseId);
    	ExpenseDTO expenseDTO = expenseService.getExpenseByExpenseId(expenseId);
    	log.info("Printing the expense details {}", expenseDTO);
    	return modelMapper.map(expenseDTO, ExpenseResponse.class);
    }

}
