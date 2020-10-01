package com.meetings.demo.repos;

import com.meetings.demo.entities.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeDB extends CrudRepository<Employee, Integer> {
}
