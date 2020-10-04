package com.meetings.demo.controllers;

import com.meetings.demo.DTOs.DepartmentDTO;
import com.meetings.demo.DTOs.EmployeeDTO;
import com.meetings.demo.DTOs.MeetingDTO;
import com.meetings.demo.entities.Department;
import com.meetings.demo.entities.Employee;
import com.meetings.demo.entities.Meeting;
import com.meetings.demo.services.MainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class MainController {
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";
    @Autowired
    private MainServiceImpl service;

    @GetMapping("/meetings")
    Iterable<MeetingDTO> getAllMeetings() {
        List<MeetingDTO> meetingDTOs = new ArrayList<>();
        for (Meeting meeting : service.findAllMeetingsOrderByTime()) {
            meetingDTOs.add(service.convertToDTO(meeting));
        }
        return meetingDTOs;
    }

    @GetMapping("/meeting/{id}")
    MeetingDTO getMeetingById(@PathVariable int id) {
        Meeting foundMeeting = service.findByMeetingId(id).orElse(null);
        return foundMeeting != null ? service.convertToDTO(foundMeeting) : null;
    }

    @GetMapping("/departments")
    Iterable<DepartmentDTO> getAllDepartments() {
        List<DepartmentDTO> departmentDTOs = new ArrayList<>();
        for (Department department : service.findAllDepartments()) {
            departmentDTOs.add(service.convertToDTO(department));
        }
        return departmentDTOs;
    }

    @GetMapping("/employees")
    Iterable<EmployeeDTO> getAllEmployees() {
        Iterable<Employee> allEmployees = service.findAllEmployees();
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        for (Employee employee : allEmployees) {
            employeeDTOs.add(service.convertToDTO(employee));
        }
        return employeeDTOs;
    }

    @GetMapping("/department/{departmentName}")
    Department getDepartment(@PathVariable String departmentName) {
        return service.findByDepartmentName(departmentName).orElse(null);
    }

    @GetMapping("/search")
    Iterable<MeetingDTO> filterSearch(@RequestParam(required = false) String theme,
                                      @RequestParam(required = false) Integer departmentId,
                                      @RequestParam(required = false) Integer employeeId,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime dateFrom,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime dateTo) {
        Iterable<Meeting> foundMeetings = service.filterSearchMeeting(theme, dateFrom, dateTo, departmentId, employeeId);
        List<MeetingDTO> searchResult = new ArrayList<>();
        for (Meeting meeting : foundMeetings) {
            searchResult.add(service.convertToDTO(meeting));
        }
        return searchResult;
    }

    @PostMapping(path = "/meeting")
    ResponseEntity<StringResponse> addMeeting(@RequestBody Meeting newMeeting) {
        if (validateMeeting(newMeeting)) {
            service.save(newMeeting);
            return new ResponseEntity("saved", HttpStatus.OK);
        }
        return new ResponseEntity("meeting collision", HttpStatus.BAD_REQUEST);
    }

    @PutMapping(path = "/meeting")
    ResponseEntity<StringResponse> updateMeeting(@RequestBody Meeting updateOfMeeting) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        Meeting meetingToUpdate = service.findByMeetingId(updateOfMeeting.getId()).orElse(null);
        if (meetingToUpdate != null) {
            if (validateMeeting(updateOfMeeting)) {
                meetingToUpdate.setTheme(updateOfMeeting.getTheme());
                meetingToUpdate.setTime(updateOfMeeting.getTime());
                meetingToUpdate.setOrganizerId(updateOfMeeting.getOrganizerId());
                meetingToUpdate.setDepartmentId(updateOfMeeting.getDepartmentId());
                meetingToUpdate.setParticipantIds(updateOfMeeting.getParticipantIds());
                service.save(meetingToUpdate);
                return new ResponseEntity("updated", HttpStatus.OK);
            }
            return new ResponseEntity( "[\"Error\": \"meeting collision\"]", responseHeaders, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("[\"Error\": \"meeting not found\"]", responseHeaders,HttpStatus.BAD_REQUEST);
    }

    boolean validateMeeting(Meeting meeting) {
//        List<Meeting> simultMeetings = service.findAllByMeetingTime(meeting.getTime());
//        Collection<Integer> participantIds = meeting.getParticipantIds();
//        if (!simultMeetings.isEmpty()) {
//            for (Meeting existingMeeting : simultMeetings) {
//                Collection<Integer> exMeetingParticipantIds = existingMeeting.getParticipantIds();
//                int initSize = exMeetingParticipantIds.size();
//                exMeetingParticipantIds.retainAll(participantIds);
//                if (initSize != exMeetingParticipantIds.size()) {
//                    return false;
//                }
//            }
//        }
        return true;
    }
}
