package com.endava.employee.documents;

import java.util.Date;

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
@Document(collection = "achievement")
public class Achievement {
	
	@Id
	private String id;
	
	private String description;
	
	private Employee employee;
	
	private Date dateAchievement;
	
}
