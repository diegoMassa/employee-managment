package com.endava.employee.services;

import com.endava.employee.documents.Grade;

import reactor.core.publisher.Mono;

public interface IGradeService {

	public Mono<Grade> save(Grade grade);
	
	public Mono<Grade> findById(String id);
	
}
