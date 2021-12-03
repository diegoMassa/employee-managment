package com.endava.employee.documents;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "document_type")
public class DocumentType {
	
	@Id
	private String id;
	
	@NotNull
	private String name;
	
	private String state;

}
