package com.endava.employee.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.endava.employee.documents.Employee;

import reactor.core.publisher.Mono;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String>{

	public Mono<Employee> findByDocumentNumber(String docuNumber);
	
}
