package com.meetings.demo.DTOs;

import java.time.LocalDateTime;
import java.util.List;

public class MeetingDTO {
    int id;
    String theme;
    //String department;
    //String organizer;
    EmployeeDTO organizer;
    List<String> participantNames;
    String time;
    int count;

    DepartmentDTO department;
    List<EmployeeDTO> participants;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public List<String> getParticipantNames() {
        return participantNames;
    }

    public void setParticipantNames(List<String> participantNames) {
        this.participantNames = participantNames;
    }

//    public String getDepartment() {
//        return department;
//    }
//
//    public void setDepartment(String department) {
//        this.department = department;
//    }

//    public String getOrganizer() {
//        return organizer;
//    }
//
//    public void setOrganizer(String organizer) {
//        this.organizer = organizer;
//    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    public List<EmployeeDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<EmployeeDTO> participants) {
        this.participants = participants;
    }

    public EmployeeDTO getOrganizer() {
        return organizer;
    }

    public void setOrganizer(EmployeeDTO organizer) {
        this.organizer = organizer;
    }
}
