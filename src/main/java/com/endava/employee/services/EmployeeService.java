package com.endava.employee.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.endava.employee.documents.DocumentType;
import com.endava.employee.documents.Employee;
import com.endava.employee.documents.Grade;
import com.endava.employee.dto.AchievementResponseDTO;
import com.endava.employee.dto.EmployeeDTO;
import com.endava.employee.dto.EmployeeResponseDTO;
import com.endava.employee.mapper.EmployeeMapper;
import com.endava.employee.repository.DocumentTypeRepository;
import com.endava.employee.repository.EmployeeRepository;
import com.endava.employee.repository.GradeRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 * @author dmunoz
 *
 */
@Service
public class EmployeeService implements IEmployeeService{

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private DocumentTypeRepository documentTypeRepository;
	
	@Autowired
	private GradeRepository gradeRepository;
	
	@Autowired
	private IAchievementService achievementService;
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	
	/**
	 * 
	 */
	@Override
	public Mono<Employee> save(EmployeeDTO employeeDTO) {
		Mono<Employee> mEmployee = employeeMapper.convertEmployeeDTOToMonoEmployee(employeeDTO);
		return mEmployee.flatMap(employeeRepository::save);
	}
	
	/**
	 * 
	 */
	@Override
	public Mono<EmployeeResponseDTO> saveWithAchievements(EmployeeDTO employeeDTO){
		Mono<DocumentType> mDocType = documentTypeRepository.findById(employeeDTO.getDocumentTypeId());
		Mono<Grade> mGrade =  gradeRepository.findById(employeeDTO.getGradeId());
		return  Mono.zip(mDocType, mGrade, (responseDocType, responseGrade) ->  
						employeeMapper.convertEmployeeDTOToEmployee(employeeDTO, responseDocType, responseGrade))
		.flatMap(employeeRepository::save)
		.flatMap(employeeSaved -> {
			return achievementService.saveAchievementsOfEmployee(employeeDTO, employeeSaved)
					.flatMap(achiResponseDTO -> createResponse(employeeSaved, achiResponseDTO));
		});
	}

	/**
	 * Find all employees
	 */
	@Override
	public Flux<EmployeeDTO> findAll() {
		return employeeRepository.findAll().flatMap(employee -> {
			return achievementService.listAchievementByEmployeeId(employee.getId())
					.switchIfEmpty(Flux.empty())
					.collectList()
					.flatMap(achievementList -> {
						return Mono.just(employeeMapper.convertEmployeeToEmployeeDTO(employee, achievementList));
					});
		});
	}
	
	/**
	 * Find Employee By Id
	 */
	@Override
	public Mono<EmployeeDTO> findById(String id) {
		return employeeRepository.findById(id).flatMap(employee -> {
			return achievementService.listAchievementByEmployeeId(employee.getId())
					.switchIfEmpty(Mono.empty())
					.collectList()
					.flatMap(achievementList -> {
						return Mono.just(employeeMapper.convertEmployeeToEmployeeDTO(employee, achievementList));
					});
			});
	}
	
	/**
	 * find Employee by DocumentNumber
	 */
	@Override
	public Mono<EmployeeDTO> findByDocNumber(String docuNumber) {
		return employeeRepository.findByDocumentNumber(docuNumber)
				.switchIfEmpty(Mono.empty())
				.flatMap(employee -> {
					return achievementService.listAchievementByEmployeeId(employee.getId()).switchIfEmpty(Mono.empty())
							.collectList().flatMap(achievementList -> {
								return Mono
										.just(employeeMapper.convertEmployeeToEmployeeDTO(employee, achievementList));
							});
				});
	}

	/**
	 * Delete employee With Achievements
	 */
	@Override
	public Mono<Void> deleteEmployeeWithAchievements(EmployeeDTO employeeDTO) {
		return employeeMapper.convertEmployeeDTOToMonoEmployee(employeeDTO)
				.doOnNext(employeeToDelete -> achievementService
						.deleteAchievementsByEmployeeId(employeeToDelete.getId()).subscribe())
				.flatMap(employeeRepository::delete);
	}
	
	
	/**
	 * 
	 * @param employeeDTO
	 * @param employeeResponse
	 * @param achResponseDTO 
	 * @return
	 */
	private Mono<EmployeeResponseDTO> createResponse(Employee employeeResponse, AchievementResponseDTO achiDTO) {
		return Mono.just(
				EmployeeResponseDTO.builder()
				.id(employeeResponse.getId())
				.documentNumber(employeeResponse.getDocumentNumber())
				.documentTypeDescription(employeeResponse.getDocumentType().getName())
				.email(employeeResponse.getEmail())
				.firstName(employeeResponse.getEmail())
				.lastName(employeeResponse.getLastName())
				.dateOfBirth(employeeResponse.getDateOfBirth())
				.gradeDescription(employeeResponse.getGrade().getName())
				.listAchievements(achiDTO.getListAchievementDTO())
				.build()
				);
	}
}