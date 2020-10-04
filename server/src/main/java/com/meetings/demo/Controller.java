package com.meetings.demo;

import com.meetings.demo.DTOs.DepartmentDTO;
import com.meetings.demo.DTOs.EmployeeDTO;
import com.meetings.demo.DTOs.MeetingDTO;
import com.meetings.demo.entities.Department;
import com.meetings.demo.entities.Employee;
import com.meetings.demo.entities.Meeting;
import com.meetings.demo.services.MainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class Controller {
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";
    @Autowired
    private MainServiceImpl service;

//    @GetMapping("/department/{departmentName}")
//    List<Employee> getDepartmentEmployees(@PathVariable String deparmentName){
//        return departmentDB.findByName(deparmentName).orElse(null).getMembers();
//    }

    @GetMapping("/meetings")
    Iterable<MeetingDTO> getAllMeetings(){
        List<MeetingDTO> meetingDTOs = new ArrayList<>();
        for (Meeting meeting : service.findAllMeetings()){
            meetingDTOs.add(service.convertToDTO(meeting));
        }
        return meetingDTOs;
    }

    @GetMapping("/meeting/{id}")
    MeetingDTO getMeetingById(@PathVariable int id){
        System.out.println("in get meeting: " + id);
        Meeting foundMeeting  = service.findByMeetingId(id).orElse(null);
        return service.convertToDTO(foundMeeting);
    }

    @GetMapping("/departments")
    Iterable<DepartmentDTO> getAllDepartments(){
        List<DepartmentDTO> departmentDTOs = new ArrayList<>();
        for (Department department : service.findAllDepartments()){
            departmentDTOs.add(service.convertToDTO(department));
        }
        return departmentDTOs;
    }

    @GetMapping("/employees")
    Iterable<EmployeeDTO> getAllEmployees(){
        Iterable<Employee> allEmployees =  service.findAllEmployees();
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        System.out.println(allEmployees);
        for (Employee employee : allEmployees){
            employeeDTOs.add(service.convertToDTO(employee));
        }
        System.out.println(employeeDTOs);
        return employeeDTOs;
    }

    @GetMapping("/department/{departmentName}")
    Department getDepartment(@PathVariable String departmentName){
        return service.findByDepartmentName(departmentName).orElse(null);
    }

    @GetMapping("/search")
    Iterable<MeetingDTO> filterSearch(@RequestParam(required = false) String theme,
                                   @RequestParam(required = false) Integer departmentId,
                                   @RequestParam(required = false) Integer employeeId,
                                   @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime dateFrom,
                                   @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime dateTo){
        System.out.println("searching...");
        System.out.println("departmentId = " + departmentId + " emplyeeId = " + employeeId + " fromDate = " + dateFrom + " toDate = " + dateTo);
        Iterable<Meeting> foundMeetings =  service.filterSearchMeeting(theme, dateFrom, dateTo,departmentId, employeeId);
        System.out.println("FOUND MEETINGS: ");
        System.out.println(foundMeetings);

        List<MeetingDTO> searchResult = new ArrayList<>();
        for (Meeting meeting : foundMeetings){
            searchResult.add(service.convertToDTO(meeting));
        }
        return searchResult;
    }

    @PostMapping(path = "/department/{departmentName}")
    String addDepartment(@PathVariable String departmentName) {
        if (service.findByDepartmentName(departmentName).isEmpty()) service.save(new Department(departmentName));
        return "finished";
    }


    @PostMapping(path = "/employee/{departmentName}")
    String addEmployee(@PathVariable String departmentName, @RequestBody Employee newEmployee) {
        Department department = service.findByDepartmentName(departmentName).orElse(null);
        if (department!=null){
            newEmployee.setDepartment(department);
            service.save(newEmployee);
            department.getMembers().add(newEmployee);
            service.save(department);
            //employeeDB.save(newEmployee);
        }
        return "finished";
    }

    @PostMapping(path = "/meeting")
    String addMeeting(@RequestBody Meeting newMeeting) {
        System.out.println("new Meeting: " + newMeeting);
        Meeting saveMeeting = new Meeting();
        if (service.findByMeetingTheme(newMeeting.getTheme()).isEmpty()){
            //newMeeting.getParticipantIds().add(newMeeting.getOrganizerId());
            service.save(newMeeting);
        }
        Meeting savedMeeting = service.findByMeetingTheme(newMeeting.getTheme()).orElse(null);
        System.out.println("savedMeeting: " + savedMeeting);
        return "finished";
    }

//    @GetMapping("/meeting/{meetingTheme}")
//    Iterable<Employee> getAllEmployees(@PathVariable  String meetingTheme){
//        return  meetingTheme.findByName(meetingTheme).orElse;
//    }

}
