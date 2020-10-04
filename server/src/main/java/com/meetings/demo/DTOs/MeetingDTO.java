package com.meetings.demo.DTOs;

import java.util.List;

public class MeetingDTO {
    int id;
    String theme;
    EmployeeDTO organizer;
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
