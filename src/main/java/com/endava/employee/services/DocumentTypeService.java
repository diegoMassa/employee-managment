package com.endava.employee.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.endava.employee.documents.DocumentType;
import com.endava.employee.repository.DocumentTypeRepository;

import reactor.core.publisher.Mono;

@Service
public class DocumentTypeService implements IDocumentTypeService {
	
	@Autowired
	private DocumentTypeRepository documentTypeRepository;

	@Override
	public Mono<DocumentType> save(DocumentType documentType) {
		return documentTypeRepository.save(documentType);
	}

	@Override
	public Mono<DocumentType> findById(String id) {
		return documentTypeRepository.findById(id);
	} 
}
