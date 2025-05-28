package com.project.spring.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.project.spring.dto.ExpenseDTO;
import com.project.spring.entity.ExpenseEntity;
import com.project.spring.entity.ProfileEntity;
import com.project.spring.exception.ResourceNotFoundException;
import com.project.spring.repository.ExpenseRepository;
import com.project.spring.service.AuthService;
import com.project.spring.service.ExpenseService;

import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/*
 * Service implementation for Expense module
 */
@Service
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {
    
	private final ExpenseRepository expenseRepository;
	private final ModelMapper modelMapper;
	private final AuthService authService;
	
    public ExpenseServiceImpl (ExpenseRepository expenseRepository, ModelMapper modelMapper, AuthService authService) {
    	this.expenseRepository = expenseRepository;
    	this.modelMapper = modelMapper;
    	this.authService = authService;
    }

    /**
     * It will fetch the expenses from database
     * @return list
     */
    @Override
    public List<ExpenseDTO> getAllExpenses() {
    	Long loggedInProfileId = authService.getLoggedInProfile().getId();
        List<ExpenseEntity> list = expenseRepository.findByOwnerId(loggedInProfileId);
        return list.stream()
                   .map(expense -> modelMapper.map(expense, ExpenseDTO.class))
                   .collect(Collectors.toList());
    }

    /**
     * It will fetch expense of the owner from database
     * @param expenseId
     * @return ExpenseDTO
     */
    @Override
    public ExpenseDTO getExpenseByExpenseId(String expenseId) {
    	Long id = authService.getLoggedInProfile().getId();
    	ExpenseEntity expenseEntity = expenseRepository.findByOwnerIdAndExpenseId(id, expenseId)
    			.orElseThrow(() -> new ResourceNotFoundException("Expense not found for the expense id " + expenseId));
    	log.info("Printing the expense entity details {}", expenseEntity);
    	return modelMapper.map(expenseEntity, ExpenseDTO.class);
    }
    
    /**
     * It will delete an expense from database
     * @param expenseId
     * @return void
     */
    @Override
    public void deleteExpenseByExpenseId(String expenseId) {
    	ExpenseEntity expenseEntity = getExpenseEntity(expenseId);
    	log.info("Printing the expense entity {}", expenseEntity);
    	expenseRepository.delete(expenseEntity);
    }
    
    /**
     * Fetch the expense of the owner by expenseId from database
     * @param expenseId
     * @return ExpenseEntity
     */
    private ExpenseEntity getExpenseEntity(String expenseId) {
    	Long id = authService.getLoggedInProfile().getId();
    	return expenseRepository.findByOwnerIdAndExpenseId(id, expenseId)
    			.orElseThrow(() -> new ResourceNotFoundException("Expense not found for the expense id " + expenseId));
    }
    
    /**
     * It will save the expense details to database
     * @param expenseDTO
     * @return ExpenseDTO
     */
    @Override
    public ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO) {
    	ProfileEntity profileEntity = authService.getLoggedInProfile();
    	ExpenseEntity expenseEntity = modelMapper.map(expenseDTO, ExpenseEntity.class);
    	expenseEntity.setExpenseId(UUID.randomUUID().toString());
    	expenseEntity.setOwner(profileEntity);
    	expenseEntity = expenseRepository.save(expenseEntity);
    	log.info("Printing the new expense entity details {}", expenseEntity);
    	return modelMapper.map(expenseEntity, ExpenseDTO.class);
    }
    
    /**
     * It will update the expense details to database
     * @param expenseDTO
     * @param expenseId
     * @return ExpenseDTO
     */
    @Override
    public ExpenseDTO updateExpenseDetails(ExpenseDTO expenseDTO, String expenseId) {
    	ExpenseEntity oldExpenseEntity = getExpenseEntity(expenseId);
    	ExpenseEntity newExpenseEntity = modelMapper.map(expenseDTO, ExpenseEntity.class);
    	newExpenseEntity.setId(oldExpenseEntity.getId());
    	newExpenseEntity.setExpenseId(oldExpenseEntity.getExpenseId());
    	newExpenseEntity.setCreatedAt(oldExpenseEntity.getCreatedAt());
    	newExpenseEntity.setUpdatedAt(oldExpenseEntity.getUpdatedAt());
    	newExpenseEntity.setOwner(oldExpenseEntity.getOwner());
    	newExpenseEntity = expenseRepository.save(newExpenseEntity);
    	log.info("Printing the udpated expense entity details {}", newExpenseEntity);
    	return modelMapper.map(newExpenseEntity, ExpenseDTO.class);
    }
  
}
