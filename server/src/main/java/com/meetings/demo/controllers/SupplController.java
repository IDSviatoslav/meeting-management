package com.meetings.demo.controllers;

import com.meetings.demo.entities.Department;
import com.meetings.demo.entities.Employee;
import com.meetings.demo.services.MainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SupplController {
    @Autowired
    private MainServiceImpl service;

    @PostMapping(path = "/department/{departmentName}")
    String addDepartment(@PathVariable String departmentName) {
        if (service.findByDepartmentName(departmentName).isEmpty()) {
            service.save(new Department(departmentName));
        }
        return "finished";
    }


    @PostMapping(path = "/employee/{departmentName}")
    String addEmployee(@PathVariable String departmentName, @RequestBody Employee newEmployee) {
        Department department = service.findByDepartmentName(departmentName).orElse(null);
        if (department != null) {
            newEmployee.setDepartment(department);
            service.save(newEmployee);
            department.getMembers().add(newEmployee);
            service.save(department);
        }
        return "finished";
    }
}
