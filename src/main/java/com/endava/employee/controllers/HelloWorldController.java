package com.endava.employee.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.endava.employee.dto.EmployeeDTO;
import com.endava.employee.services.IEmployeeService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/employee")
public class HelloWorldController {

	@Autowired
	private IEmployeeService employeeService;

	@GetMapping
	public Flux<EmployeeDTO> getAllEmployees(){
		return employeeService.findAll();
	}
	
	@GetMapping("/{id}")
	public Mono<EmployeeDTO> getAEmployeeById(@PathVariable String id){
		return employeeService.findById(id);
	}
}
