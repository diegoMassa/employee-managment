package com.endava.employee.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.endava.employee.documents.Achievement;
import com.endava.employee.documents.Employee;
import com.endava.employee.dto.AchievementDTO;
import com.endava.employee.dto.AchievementResponseDTO;
import com.endava.employee.dto.EmployeeDTO;
import com.endava.employee.mapper.AchievementMapper;
import com.endava.employee.repository.AchievementRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 * @author dmunoz
 *
 */
@Service
public class AchievementService implements IAchievementService {

	@Autowired
	private AchievementRepository achievementRepository;
	
	@Autowired
	private AchievementMapper achiMapper;
	
	
	
	/**
	 * Find All
	 */
	@Override
	public Flux<Achievement> findAll() {
		return achievementRepository.findAll();
	}
	
	
	/**
	 * 
	 * @param employeeDTO
	 * @param employeeResponse
	 */
	@Override
	public Mono<AchievementResponseDTO> saveAchievementsOfEmployee(EmployeeDTO employeeDTO, Employee employeeResponse) {
		if (employeeDTO.getListAchievements() != null && !employeeDTO.getListAchievements().isEmpty()) {
			return Mono.just(employeeDTO)
					.flatMap(e -> Mono.just(e.getListAchievements()))
					.flatMapMany(Flux::fromIterable)
					.flatMap(achi -> achievementRepository.save(achiMapper.convertAchievementDTOToAchievement(achi, employeeResponse)))
					.map(achiMapper::convertAchievementToAchievementDTO)
					.collectList()
					.map(achvListSaved -> AchievementResponseDTO.builder().listAchievementDTO(achvListSaved).build());
		}
		return Mono.just(AchievementResponseDTO.builder().build());
	}
	

	/**
	 * 
	 * @param employeeId
	 * @return
	 */
	@Override
	public Flux<AchievementDTO> listAchievementByEmployeeId(String employeeId){
		return achievementRepository.findAll()
				.filter(ach -> ach.getEmployee().getId().equals(employeeId))
				.map(achiMapper::convertAchievementToAchievementDTO);
	}


	/**
	 * Delete Achievements By Employee
	 */
	@Override
	public Flux<Void> deleteAchievementsByEmployeeId(String employeeId) {
		Flux<Achievement> achievementByEmployee = achievementRepository.findAll()
				.filter(ach -> ach.getEmployee().getId().equals(employeeId));
		return achievementByEmployee.flatMap(achievementRepository::delete);
	}
}