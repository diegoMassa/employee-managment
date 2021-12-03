package com.endava.employee.mapper;

import org.springframework.stereotype.Service;

import com.endava.employee.documents.Achievement;
import com.endava.employee.documents.Employee;
import com.endava.employee.dto.AchievementDTO;

@Service
public class AchievementMapper {
	
	/**
	 * 
	 * @param achievement
	 * @return AchievementDTO achievementDTO
	 */
	public AchievementDTO convertAchievementToAchievementDTO(Achievement achievement) {
		return AchievementDTO.builder()
				.id(achievement.getId())
				.description(achievement.getDescription())
				.achievementDate(achievement.getDateAchievement())
				.build();
	}
	
	/**
	 * 
	 * @param achievementDTO
	 * @param employee
	 * @return
	 */
	public Achievement convertAchievementDTOToAchievement(AchievementDTO achievementDTO, Employee employee) {
		return Achievement.builder()
				.id(achievementDTO.getId())
				.description(achievementDTO.getDescription())
				.employee(employee)
				.dateAchievement(achievementDTO.getAchievementDate())
				.build();
	}
}
