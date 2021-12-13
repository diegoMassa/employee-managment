package com.endava.employee.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.endava.employee.documents.Grade;
import com.endava.employee.repository.GradeRepository;

import reactor.core.publisher.Mono;

@Service
public class GradeService implements IGradeService{

	@Autowired
	private GradeRepository gradeRepository;
	
	@Override
	public Mono<Grade> save(Grade grade) {
		return gradeRepository.save(grade);
	}

	@Override
	public Mono<Grade> findById(String id) {
		return gradeRepository.findById(id);
	}
}
