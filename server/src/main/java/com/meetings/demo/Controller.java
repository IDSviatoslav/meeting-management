package com.meetings.demo;

import com.meetings.demo.DTOs.EmployeeDTO;
import com.meetings.demo.entities.Department;
import com.meetings.demo.entities.Employee;
import com.meetings.demo.entities.Meeting;
import com.meetings.demo.repos.DepartmentDB;
import com.meetings.demo.repos.EmployeeDB;
import com.meetings.demo.repos.MeetingDB;
import com.meetings.demo.services.MainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class Controller {
    @Autowired
    private DepartmentDB departmentDB;
    @Autowired
    private EmployeeDB employeeDB;
    @Autowired
    private MeetingDB meetingDB;

    private static final String DATE_PATTERN = "dd-mm-yyyy";


    @Autowired
    private MainServiceImpl mainService;

//    @GetMapping("/department/{departmentName}")
//    List<Employee> getDepartmentEmployees(@PathVariable String deparmentName){
//        return departmentDB.findByName(deparmentName).orElse(null).getMembers();
//    }

    @GetMapping("/meetings")
    Iterable<Meeting> getAllMeetings(){
        System.out.println("in all meetings");
        return meetingDB.findAll();
    }

    @GetMapping("/departments")
    Iterable<Department> getAllDepartments(){
        System.out.println("in all departments");
        return departmentDB.findAll();
    }

    @GetMapping("/employees")
    Iterable<EmployeeDTO> getAllEmployees(){
        Iterable<Employee> allEmployees =  employeeDB.findAll();
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        System.out.println(allEmployees);
        for (Employee employee : allEmployees){
            employeeDTOs.add(EmployeeDTO.convertToDTO(employee));
        }
        System.out.println(employeeDTOs);
        return employeeDTOs;
    }

    @GetMapping("/department/{departmentName}")
    Department getDepartment(@PathVariable String departmentName){
        return departmentDB.findByName(departmentName).orElse(null);
    }

    @GetMapping("/search")
    Iterable<Meeting> filterSearch(@RequestParam(required = false) String theme,
                                   @RequestParam(required = false) Integer departmentId,
                                   @RequestParam(required = false) Integer employeeId,
                                   @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date dateFrom,
                                   @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date dateTo){
        System.out.println("searching...");
        System.out.println("departmentId = " + departmentId + " emplyeeId = " + employeeId + " fromDate = " + dateFrom + " toDate = " + dateTo);
        Iterable<Meeting> found =  mainService.filterSearchMeeting(theme, dateFrom, dateTo,departmentId, employeeId);
        System.out.println(found);
        return found;
    }

    @PostMapping(path = "/department/{departmentName}")
    String addDepartment(@PathVariable String departmentName) {
        if (departmentDB.findByName(departmentName).isEmpty()) departmentDB.save(new Department(departmentName));
        return "finished";
    }


    @PostMapping(path = "/employee/{departmentName}")
    String addEmployee(@PathVariable String departmentName, @RequestBody Employee newEmployee) {
        Department department = departmentDB.findByName(departmentName).orElse(null);
        if (department!=null){
            department.getMembers().add(newEmployee);
            departmentDB.save(department);
            //employeeDB.save(newEmployee);
        }
        return "finished";
    }

    @PostMapping(path = "/meeting")
    String addMeeting(@RequestBody Meeting newMeeting) {
        System.out.println("new Meeting: " + newMeeting);
        Meeting saveMeeting = new Meeting();
        if (meetingDB.findByTheme(newMeeting.getTheme()).isEmpty()){
            meetingDB.save(newMeeting);
        }
        Meeting savedMeeting = meetingDB.findByTheme(newMeeting.getTheme()).orElse(null);
        System.out.println("savedMeeting: " + savedMeeting);
        return "finished";
    }

//    @GetMapping("/meeting/{meetingTheme}")
//    Iterable<Employee> getAllEmployees(@PathVariable  String meetingTheme){
//        return  meetingTheme.findByName(meetingTheme).orElse;
//    }

}
