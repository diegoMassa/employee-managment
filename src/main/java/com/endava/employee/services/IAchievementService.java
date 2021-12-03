package com.endava.employee.services;

import com.endava.employee.documents.Achievement;
import com.endava.employee.documents.Employee;
import com.endava.employee.dto.AchievementDTO;
import com.endava.employee.dto.AchievementResponseDTO;
import com.endava.employee.dto.EmployeeDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAchievementService {

	public Mono<AchievementResponseDTO> saveAchievementsOfEmployee(EmployeeDTO employeeDTO, Employee employeeResponse);
	
	public Flux<Achievement> findAll();
	
	public Flux<AchievementDTO> listAchievementByEmployeeId(String employeeNumberId);
	
	public Flux<Void> deleteAchievementsByEmployeeId(String employeeId);
	
}
