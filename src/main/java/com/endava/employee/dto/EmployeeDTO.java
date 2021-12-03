package com.endava.employee.dto;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

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
public class EmployeeDTO {

	private String id;
	
	@Valid
	@NotNull(message = "it's mandatory")
	private String documentTypeId;
	
	private String documentTypeDescription;
	
	@NotNull(message = "it's mandatory")
	private String documentNumber;
	
	@NotNull(message = "it's mandatory")
	private String firstName;
	
	private String lastName;
	
	@Email
	private String email;
	
	@Valid
	@NotNull(message = "it's mandatory")
	private String gradeId;
	
	private String gradeDescription;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateOfBirth;
	
	@Valid
	private List<AchievementDTO> listAchievements;
}
