package com.meetings.demo.DTOs;

import javax.persistence.Embeddable;
import java.util.List;

@Embeddable
public class DepartmentDTO {
    int id;
    String name;

    List<EmployeeDTO> members;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EmployeeDTO> getMembers() {
        return members;
    }

    public void setMembers(List<EmployeeDTO> members) {
        this.members = members;
    }

}
