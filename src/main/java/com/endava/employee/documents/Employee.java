package com.endava.employee.documents;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Data
@Document(collection = "employee")
public class Employee {

	@Id
	private String id;
	
	private DocumentType documentType;
	
	private String documentNumber;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private Grade grade;
	
	private Date dateOfBirth;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date creationDate;
}