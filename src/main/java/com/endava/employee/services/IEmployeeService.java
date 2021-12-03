package com.endava.employee.services;

import com.endava.employee.documents.Employee;
import com.endava.employee.dto.EmployeeDTO;
import com.endava.employee.dto.EmployeeResponseDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IEmployeeService {
	
	public Mono<Employee> save(EmployeeDTO employee);

	public Mono<EmployeeResponseDTO> saveWithAchievements(EmployeeDTO employee);
	
	public Flux<EmployeeDTO> findAll();
	
	public Mono<EmployeeDTO> findById(String id); 
	
	public Mono<EmployeeDTO> findByDocNumber(String docNumber); 
	
	public Mono<Void> deleteEmployeeWithAchievements(EmployeeDTO employee);

}