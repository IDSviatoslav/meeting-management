package com.meetings.demo.services;

import com.meetings.demo.entities.Department;
import com.meetings.demo.entities.Employee;
import com.meetings.demo.entities.Meeting;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MainService {
    Iterable<Meeting> filterSearchMeeting(String theme, LocalDateTime fromDate, LocalDateTime toDate, Integer departmentId, Integer participantName);
    Iterable<Meeting> findAllMeetings();
    Iterable<Department> findAllDepartments();
    Iterable<Employee> findAllEmployees();
    void save(Meeting newMeeting);
    void save(Department newDepartment);
    void save(Employee newEmployee);
    Optional<Department> findByDepartmentName(String departmentName);
    Optional<Meeting> findByMeetingTheme(String meetingName);
    Optional<Meeting> findByMeetingId(int id);

}
