package com.endava.employee.mapper;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.endava.employee.documents.DocumentType;
import com.endava.employee.documents.Employee;
import com.endava.employee.documents.Grade;
import com.endava.employee.dto.AchievementDTO;
import com.endava.employee.dto.EmployeeDTO;
import com.endava.employee.repository.DocumentTypeRepository;
import com.endava.employee.repository.GradeRepository;

import reactor.core.publisher.Mono;

@Service
public class EmployeeMapper {
	
	
	@Autowired
	private DocumentTypeRepository documentTypeRepository;
	
	@Autowired
	private GradeRepository gradeRepository;
	
	
	/**
	 * 
	 * @param employee
	 * @param listAchievements
	 * @return
	 */
	public EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee, List<AchievementDTO> listAchievements) {
		return EmployeeDTO.builder()
				.id(employee.getId())
				.documentNumber(employee.getDocumentNumber())
				.dateOfBirth(employee.getDateOfBirth())
				.documentTypeId(employee.getDocumentType().getId())
				.documentTypeDescription(employee.getDocumentType().getName())
				.email(employee.getEmail())
				.firstName(employee.getFirstName())
				.lastName(employee.getLastName())
				.gradeId(employee.getGrade().getId())
				.gradeDescription(employee.getGrade().getName())
				.listAchievements(listAchievements)
				.build();
	}

	/**
	 * 
	 * @param employeeDTO
	 * @return
	 */
	public Mono<Employee> convertEmployeeDTOToMonoEmployee(EmployeeDTO employeeDTO) {
		
		Mono<DocumentType> mDocType = documentTypeRepository.findById(employeeDTO.getDocumentTypeId());
		Mono<Grade> mGrade =  gradeRepository.findById(employeeDTO.getGradeId());
		return Mono.zip(mDocType, mGrade, (docT, grade) ->
			 Employee.builder()
			 		.id(employeeDTO.getId())
					.creationDate(new Date())
					.dateOfBirth(null)
					.documentNumber(employeeDTO.getDocumentNumber())
					.documentType(docT)
					.email(employeeDTO.getEmail())
					.firstName(employeeDTO.getFirstName())
					.grade(grade)
					.lastName(employeeDTO.getLastName())
					.build()
		);
	}
	
	/**
	 * 
	 * @param employeeDTO
	 * @param response
	 * @return
	 */
	public Employee convertEmployeeDTOToEmployee(EmployeeDTO employeeDTO, DocumentType docTypeEnt, Grade gradeEnt) {
		return	Employee.builder()
				.id(employeeDTO.getId())
				.creationDate(new Date())
				.dateOfBirth(employeeDTO.getDateOfBirth())
				.documentNumber(employeeDTO.getDocumentNumber())
				.documentType(docTypeEnt)
				.email(employeeDTO.getEmail())
				.firstName(employeeDTO.getFirstName())
				.grade(gradeEnt)
				.lastName(employeeDTO.getLastName())
				.build();
	}
}
