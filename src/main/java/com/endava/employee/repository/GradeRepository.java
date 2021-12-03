package com.endava.employee.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.endava.employee.documents.Grade;

public interface GradeRepository extends ReactiveMongoRepository<Grade, String>{

}
