package com.project.spring.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.spring.dto.ExpenseDTO;
import com.project.spring.io.ExpenseRequest;
import com.project.spring.io.ExpenseResponse;
import com.project.spring.service.ExpenseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin("*")
@Slf4j
@Tag(name = "Expense Constroller", description = "Expense Controller")
@RequestMapping(path = "/expenses")
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
    @GetMapping()
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
    @GetMapping("/{expenseId}")
    public ExpenseResponse getExpenseById(@PathVariable String expenseId) {
    	log.info("API GET /expenses/{} is called", expenseId);
    	ExpenseDTO expenseDTO = expenseService.getExpenseByExpenseId(expenseId);
    	log.info("Printing the expense details {}", expenseDTO);
    	return modelMapper.map(expenseDTO, ExpenseResponse.class);
    }
    
    /**
     * It will delete the expense from service
     * @param expenseId
     * @return void
     */
    @Operation(summary = "Delete an expense by expenseId", 
    			description = "Returns void after deleting the expense")
    @DeleteMapping("/{expenseId}")
    public void deleteExpenseByExpenseId(@PathVariable String expenseId) {
    	log.info("API DELETE /expenses/{}", expenseId);
    	expenseService.deleteExpenseByExpenseId(expenseId);
    }
    
    /**
     * It will save the expense details to database
     * @param expenseRequest
     * @return ExpenseResponse
     */
    @Operation(summary = "Post expense details",
    			description = "Returns an expense response")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public ExpenseResponse saveExpenseDetails(@RequestBody @Valid ExpenseRequest expenseRequest) {
    	log.info("API POST /expenses called {}", expenseRequest);
    	ExpenseDTO expenseDTO = modelMapper.map(expenseRequest, ExpenseDTO.class);
    	expenseDTO = expenseService.saveExpenseDetails(expenseDTO);
    	log.info("Printing the ExpenseDTO {}", expenseDTO);
    	return modelMapper.map(expenseDTO, ExpenseResponse.class);
    }
    
    /**
     * It will update the expense details to database
     * @param expenseRequest
     * @return ExpenseResponse
     */
    @Operation(summary = "Update expense details", 
    			description = "Returns an updated expense response")
    @PutMapping("/{expenseId}")
    public ExpenseResponse updateExpenseDetails(
    		@RequestBody ExpenseRequest updateRequest,
    		@PathVariable String expenseId) {
    	log.info("API PUT /{} request body {}", expenseId, updateRequest);
    	ExpenseDTO expenseDTO = modelMapper.map(updateRequest, ExpenseDTO.class);
    	expenseDTO = expenseService.updateExpenseDetails(expenseDTO, expenseId);
    	log.info("Printing the updated expenseDTO {}", expenseDTO);
    	return modelMapper.map(expenseDTO, ExpenseResponse.class);
    }

}
