package com.endava.employee.handler;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import java.net.URI;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.endava.employee.documents.Employee;
import com.endava.employee.dto.EmployeeDTO;
import com.endava.employee.dto.EmployeeResponseDTO;
import com.endava.employee.dto.ErrorsDTO;
import com.endava.employee.services.IEmployeeService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 * @author dmunoz
 *
 */
@Component
public class EmployeeHandler {

	@Autowired
	private IEmployeeService employeeService;
	
	@Autowired
	private Validator validator;
	
	private static final Logger log = LoggerFactory.getLogger(EmployeeHandler.class);
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> findAllEmployees(ServerRequest request){
		return ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(employeeService.findAll(), Employee.class)
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> findEmployeeById(ServerRequest request){
		return employeeService.findById(request.pathVariable("id"))
				.flatMap(employee -> ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(fromValue(employee)))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> saveEmployeeWithAchievements(ServerRequest request){
		
		Mono<EmployeeDTO> employee = request.bodyToMono(EmployeeDTO.class);
		
		return employee.flatMap(emp -> {
			
			Errors errors = new BeanPropertyBindingResult(emp, EmployeeDTO.class.getName());
			validator.validate(emp, errors);
			
			if (errors.hasErrors()) {
				return Flux.fromIterable(errors.getFieldErrors())
						.map(fieldError -> "The field " + fieldError.getField() + " " + fieldError.getDefaultMessage())
						.collectList()
						.flatMap(list -> ServerResponse.badRequest()
								.contentType(MediaType.APPLICATION_JSON)
								.body(fromValue(ErrorsDTO.builder().errors(list).build())));
			} else {
				//Validate if exist another Employee with the same DocNumber
				return employeeService.findByDocNumber(emp.getDocumentNumber())
						.flatMap(employeeBD -> ServerResponse
												.badRequest()
												.contentType(MediaType.APPLICATION_JSON)
												.body(fromValue(ErrorsDTO.builder()
																.errors(Arrays.asList("There is another employee with the same document number, please check it"))
																.build()
																)
												)
						).switchIfEmpty(ServerResponse
										.ok()
										.contentType(MediaType.APPLICATION_JSON)
									    .body(employeeService.saveWithAchievements(emp)
									    		.doOnNext(empSaved -> log.info("Employee Created: " + empSaved.toString())), 
									    		EmployeeResponseDTO.class
									    )
									    
						);
			}
		});
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> updateEmployee(ServerRequest request){
		
		//Get the Mono<Employee> from body
		Mono<EmployeeDTO> employee = request.bodyToMono(EmployeeDTO.class);
		
		return employeeService.findById(request.pathVariable("id")).zipWith(employee, (bd, req) -> {
			bd.setDateOfBirth(req.getDateOfBirth());
			bd.setDocumentNumber(req.getDocumentNumber());
			bd.setDocumentTypeId(req.getDocumentTypeId());
			bd.setEmail(req.getEmail());
			bd.setFirstName(req.getFirstName());
			bd.setGradeId(req.getGradeId());
			bd.setLastName(req.getLastName());
			bd.setListAchievements(req.getListAchievements());
			return bd;
			
		}).flatMap(emp -> ServerResponse
				.created(URI.create("/api/v2/employee/".concat(emp.getId())))
				.contentType(MediaType.APPLICATION_JSON)
				.body(employeeService.saveWithAchievements(emp), EmployeeResponseDTO.class))
		  .switchIfEmpty(ServerResponse.notFound().build());
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> deleteEmployee(ServerRequest request) {
		return employeeService.findById(request.pathVariable("id"))
				.flatMap(emploDTO -> employeeService.deleteEmployeeWithAchievements(emploDTO)
						.then(ServerResponse.noContent().build()))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public Mono<ServerResponse> welcome(ServerRequest request){
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(fromValue("Hello, welcome to reactive employee CRUD Version Final - AWS ECS"));
	}
}
