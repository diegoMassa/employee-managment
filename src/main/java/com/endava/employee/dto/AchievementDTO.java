package com.endava.employee.dto;

import java.util.Date;

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
public class AchievementDTO {

	private String id;
	
	@NotNull
	private String description;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date achievementDate; 
}
