package com.endava.employee.managment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.endava.employee.RouterFunctionConfig;
import com.endava.employee.documents.Employee;
import com.endava.employee.dto.AchievementDTO;
import com.endava.employee.dto.EmployeeDTO;
import com.endava.employee.dto.EmployeeResponseDTO;
import com.endava.employee.handler.EmployeeHandler;
import com.endava.employee.services.IDocumentTypeService;
import com.endava.employee.services.IEmployeeService;
import com.endava.employee.services.IGradeService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { RouterFunctionConfig.class, EmployeeHandler.class })
@WebFluxTest
class EmployeeManagmentApplicationTests {

	@Autowired
	private ApplicationContext context;

	@MockBean
	private IGradeService gradeService;

	@MockBean
	private IDocumentTypeService documentTypeService;

	@MockBean
	private IEmployeeService employeeService;

	private WebTestClient client;

	@Value("${config.base.endpoint.employees}")
	private String url;

	@BeforeEach
	public void setUp() {
		client = WebTestClient.bindToApplicationContext(context).build();
	}

	@Test
	public void testGetEmployeeById() {
		Employee employee = Employee.builder().id("1").firstName("ABC").email("abc@xyz.com").build();
		Mono<EmployeeDTO> employeeMono = Mono.just(EmployeeDTO.builder().id(employee.getId())
				.firstName(employee.getFirstName()).email(employee.getEmail()).build());
		
		when(employeeService.findById("1")).thenReturn(employeeMono);
		
		client.get()
				.uri(this.url + "/{id}", Collections.singletonMap("id", employee.getId()))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(EmployeeDTO.class)
				.value(userResponse -> {
					Assertions.assertThat(userResponse.getId()).isEqualTo("1");
					Assertions.assertThat(userResponse.getFirstName()).isEqualTo("ABC");
					Assertions.assertThat(userResponse.getEmail()).isEqualTo("abc@xyz.com");
				});
	}

	@Test
	public void testGetAllEmployees() {

		Employee employee1 = Employee.builder().id("1").firstName("ABC").email("abc@xyz.com").build();
		Employee employee2 = Employee.builder().id("2").firstName("XYZ").email("xyz@abc.com").build();
		
		when(employeeService.findAll()).thenReturn(Flux.just(EmployeeDTO.builder().id(employee1.getId())
				.firstName(employee1.getFirstName()).email(employee1.getEmail()).build(), 
				
		EmployeeDTO.builder()
					.id(employee2.getId())
					.firstName(employee2.getFirstName())
					.email(employee2.getEmail())
					.build()));

		client.get()
				.uri(this.url)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(EmployeeDTO.class)
				.value(userResponse -> {
					Assertions.assertThat(userResponse.get(0).getId()).isEqualTo("1");
					Assertions.assertThat(userResponse.get(0).getFirstName()).isEqualTo("ABC");
					Assertions.assertThat(userResponse.get(0).getEmail()).isEqualTo("abc@xyz.com");
					Assertions.assertThat(userResponse.get(1).getId()).isEqualTo("2");
					Assertions.assertThat(userResponse.get(1).getFirstName()).isEqualTo("XYZ");
					Assertions.assertThat(userResponse.get(1).getEmail()).isEqualTo("xyz@abc.com");
				});
	}

	
	@Test
	public void testCreateEmployeeStatus() {

		AchievementDTO achDTO = AchievementDTO.builder().achievementDate(new Date()).description("AchTest").build();
		List<AchievementDTO> achList = new ArrayList<>();
		achList.add(achDTO);
		
		EmployeeDTO employee = EmployeeDTO.builder()
				.documentTypeId("CC")
				.documentNumber("1144040220")
				.gradeId("ST")
				.firstName("ABC")
				.lastName("DEF")
				.email("abc@xyz.com")
				.listAchievements(achList)
				.build();
 
		when(employeeService.findByDocNumber(any())).thenReturn(Mono.empty());
        when(employeeService.saveWithAchievements(employee)).thenReturn(Mono.just(new EmployeeResponseDTO()));
 
        client.post()
            .uri(this.url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(employee))
            .exchange()
            .expectStatus()
            .is2xxSuccessful();
 
        verify(employeeService, times(1)).saveWithAchievements(employee);
	}
	
	
	@Test
	public void testValuesInEmployeeCreated() {
		
		EmployeeDTO employee1 = EmployeeDTO.builder()
				.documentTypeId("CC")
				.documentNumber("31916107")
				.gradeId("ST")
				.firstName("ABC")
				.lastName("DEF")
				.email("abc@xyz.com")
				.build();
		
		Mono<EmployeeResponseDTO> employeeResponseDTO = Mono
				.just(EmployeeResponseDTO.builder().firstName("ABC").email("abc@xyz.com").build());
	
		when(employeeService.findByDocNumber(any())).thenReturn(Mono.empty());
		when(employeeService.saveWithAchievements(any())).thenReturn(employeeResponseDTO);

		client.post()
				.uri(this.url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(employee1))
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(EmployeeResponseDTO.class)
				.value(userResponse -> {
					Assertions.assertThat(userResponse.getFirstName()).isEqualTo("ABC");
					Assertions.assertThat(userResponse.getEmail()).isEqualTo("abc@xyz.com");
				});
	}
	
	
	@Test
	public void testUpdateEmployee() {
		AchievementDTO achDTO = AchievementDTO.builder().achievementDate(new Date()).description("AchTest").build();
		List<AchievementDTO> achList = new ArrayList<>();
		achList.add(achDTO);
		
		EmployeeDTO employee = EmployeeDTO.builder()
				.id("1")
				.documentTypeId("CC")
				.documentNumber("1144040220")
				.gradeId("ST")
				.firstName("ABC")
				.lastName("DEF")
				.email("abc@xyz.com")
				.listAchievements(achList)
				.build();
		
		Mono<EmployeeDTO> employeeMono = Mono.just(EmployeeDTO.builder().id(employee.getId())
				.firstName(employee.getFirstName()).email(employee.getEmail()).build());
 
		when(employeeService.findById(anyString())).thenReturn(employeeMono);
        when(employeeService.saveWithAchievements(employee)).thenReturn(Mono.just(new EmployeeResponseDTO()));
 
        client.put()
        	.uri(this.url + "/{id}", Collections.singletonMap("id", employee.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(employee))
            .exchange()
            .expectStatus()
            .is2xxSuccessful();
 
        verify(employeeService, times(1)).saveWithAchievements(employee);
	}
	
	
    @Test
    void testDeleteEmployee() {
    	
    	EmployeeDTO employee = EmployeeDTO.builder()
    			.id("1")
				.documentTypeId("CC")
				.documentNumber("31916107")
				.gradeId("ST")
				.firstName("ABC")
				.lastName("DEF")
				.email("abc@xyz.com")
				.build();
    	
    	Mono<EmployeeDTO> employeeMono = Mono.just(EmployeeDTO.builder().id(employee.getId())
				.firstName(employee.getFirstName()).email(employee.getEmail()).build());
    	
        Mono<Void> voidReturn  = Mono.empty();
        
        when(employeeService.findById(anyString())).thenReturn(employeeMono);
        when(employeeService.deleteEmployeeWithAchievements(employee)).thenReturn(voidReturn);
 
        client.get()
        	.uri(this.url + "/{id}", Collections.singletonMap("id", employee.getId()))
            .exchange()
            .expectStatus()
            .isOk();
    }
}