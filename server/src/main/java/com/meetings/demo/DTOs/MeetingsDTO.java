package com.meetings.demo.DTOs;

import com.meetings.demo.entities.Employee;
import com.meetings.demo.entities.Meeting;
import com.meetings.demo.repos.DepartmentDB;
import com.meetings.demo.repos.EmployeeDB;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MeetingsDTO {
    @Autowired
    private static DepartmentDB departmentDB;
    @Autowired
    private static EmployeeDB employeeDB;

    int id;
    String theme;
    String departmentName;
    String organizerName;
    int departmentId;
    int organizerId;
    List<String> participantNames;

    public static MeetingsDTO convertToDTO(Meeting meeting){
        MeetingsDTO conversion = new MeetingsDTO();
        conversion.setTheme(meeting.getTheme());
        conversion.setDepartmentName(departmentDB.findById(meeting.getDepartmentId()).toString());
        conversion.setOrganizerName(departmentDB.findById(meeting.getOrganizerId()).toString());
        List<String> participantNames = new ArrayList<>();
        for (int participantId : meeting.getParticipantIds()){
            Employee participant = employeeDB.findById(participantId).orElse(null);
            if (participant!=null){
                participantNames.add(participant.getSurname() + " " + participant.getName() + " " + participant.getPatronymic());
            }
        }
        conversion.setParticipantNames(participantNames);
        return conversion;
    }

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

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }

    public List<String> getParticipantNames() {
        return participantNames;
    }

    public void setParticipantNames(List<String> participantNames) {
        this.participantNames = participantNames;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }
}
