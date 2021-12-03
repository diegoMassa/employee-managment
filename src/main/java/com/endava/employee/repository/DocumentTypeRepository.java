package com.endava.employee.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.endava.employee.documents.DocumentType;

public interface DocumentTypeRepository extends ReactiveMongoRepository<DocumentType, String>{

}
