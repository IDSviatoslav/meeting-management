package com.meetings.demo.repos;

import com.meetings.demo.entities.Department;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DepartmentDB extends CrudRepository<Department, Integer> {
    Optional<Department> findByName(String name);
}
