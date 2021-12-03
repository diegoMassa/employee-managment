package com.endava.employee.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.endava.employee.documents.Achievement;

public interface AchievementRepository extends ReactiveCrudRepository<Achievement, String> {

}
