package com.endava.employee.dto;

import java.util.Date;
import java.util.List;

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
public class EmployeeResponseDTO {

	private String id;
	private String documentTypeDescription;
	private String documentNumber;
	private String firstName;
	private String lastName;
	private String email;
	private String gradeDescription;
	private Date dateOfBirth;
	private List<AchievementDTO> listAchievements;
}
