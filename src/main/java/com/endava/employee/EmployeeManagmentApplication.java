package com.endava.employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.endava.employee.documents.DocumentType;
import com.endava.employee.documents.Grade;
import com.endava.employee.services.IDocumentTypeService;
import com.endava.employee.services.IGradeService;

import reactor.core.publisher.Flux;

/**
 * 
 * @author dmunoz
 *
 */
@SpringBootApplication
public class EmployeeManagmentApplication implements CommandLineRunner{

	@Autowired
	private ReactiveMongoTemplate mongoTemplate;
	
	@Autowired
	private IDocumentTypeService documentTypeService;
	
	@Autowired
	private IGradeService gradeService;
	
	private static final Logger log = LoggerFactory.getLogger(EmployeeManagmentApplication.class);
	
	
	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagmentApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		mongoTemplate.dropCollection("document_type").subscribe();
		mongoTemplate.dropCollection("grade").subscribe();
		mongoTemplate.dropCollection("employee").subscribe();
		mongoTemplate.dropCollection("achievement").subscribe();
		
		//Tipos de Documento
		DocumentType tipoDocumentoCC = DocumentType.builder().id("CC").name("Cedula de Ciudadania").state("A").build();
		DocumentType tipoDocumentoCE = DocumentType.builder().id("CE").name("Cedula de Extranjeria").state("A").build();
		DocumentType tipoDocumentoPasaporte = DocumentType.builder().id("PS").name("Pasaporte").state("A").build();
		
		//Grados de Endava
		Grade junior = Grade.builder().id("JT").name("Junior Developer (JT)").state("A").build();
		Grade developer = Grade.builder().id("TL").name("Developer (TL)").state("A").build();
		Grade seniorTechnisian = Grade.builder().id("ST").name("Developer (ST)").state("A").build();
		Grade engineer = Grade.builder().id("EN").name("Senior Developer (EN)").state("A").build();
		Grade seniorEngineer = Grade.builder().id("SE").name("Senior Developer (SE)").state("A").build();
		Grade disciplineLead = Grade.builder().id("CL").name("Discipline Lead (CL)").state("A").build();
		Grade developmentLead = Grade.builder().id("DCL").name("Development Lead (DCL)").state("A").build();
		
		// Save tipoDocumento
		Flux.just(tipoDocumentoCC, tipoDocumentoCE, tipoDocumentoPasaporte)
			.flatMap(documentTypeService::save)
			.doOnNext(documentType -> log.info("Document Type Created: " + documentType.toString()))
			.subscribe();
		
		//Save Grade
		Flux.just(junior, developer, seniorTechnisian, engineer, seniorEngineer, 
					disciplineLead, developmentLead)
				.flatMap(gradeService::save)
				.doOnNext(grade -> log.info("Grade Created: " + grade.toString()))
				.subscribe();
	}
}