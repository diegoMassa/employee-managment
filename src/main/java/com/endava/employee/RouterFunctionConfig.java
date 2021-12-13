package com.endava.employee;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.endava.employee.handler.EmployeeHandler;

/**
 * 
 * @author dmunoz
 *
 */
@Configuration
public class RouterFunctionConfig {

	
	@Bean
	public RouterFunction<ServerResponse> routes(EmployeeHandler handler) {
		return route(GET("/api/v2/employee"), handler::findAllEmployees)
				.andRoute(GET("/api/v2/employee/{id}"), handler::findEmployeeById)
				.andRoute(GET("/api/v2/welcome"), handler::welcome)
				.andRoute(POST("/api/v2/employee"), handler::saveEmployeeWithAchievements)
				.andRoute(PUT("/api/v2/employee/{id}"), handler::updateEmployee)
				.andRoute(DELETE("/api/v2/employee/{id}"), handler::deleteEmployee)
				;
	}
}