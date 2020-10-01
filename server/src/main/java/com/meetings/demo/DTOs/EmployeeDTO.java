package com.meetings.demo.DTOs;

import com.meetings.demo.entities.Employee;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.YEARS;

public class EmployeeDTO {
    int id;
    String name;
    String shortName;
    String department;
    long age;

    public static EmployeeDTO convertToDTO(Employee employee){
        String surname = employee.getSurname();
        String name = employee.getName();
        String patronymic = employee.getPatronymic();
        EmployeeDTO conversion = new EmployeeDTO();
        conversion.setId(employee.getId());
        conversion.setName(surname + " " + name + " " + patronymic);
        conversion.setShortName(surname + " " + name.charAt(0) + '.' + patronymic.charAt(0));
//        conversion.setDepartment(employee.getDepartment().toString());
       // conversion.setAge(YEARS.between(employee.getDateOfBirth(), LocalDate.now()));
        return conversion;
    }

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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", department='" + department + '\'' +
                ", age=" + age +
                '}';
    }
}
