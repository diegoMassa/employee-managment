package com.endava.employee.services;

import com.endava.employee.documents.DocumentType;

import reactor.core.publisher.Mono;

public interface IDocumentTypeService {

	public Mono<DocumentType> save(DocumentType documentType);
}