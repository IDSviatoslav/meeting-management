package com.meetings.demo.services;

import com.meetings.demo.DTOs.DepartmentDTO;
import com.meetings.demo.DTOs.EmployeeDTO;
import com.meetings.demo.DTOs.MeetingDTO;
import com.meetings.demo.DTOs.Person;
import com.meetings.demo.entities.Department;
import com.meetings.demo.entities.Employee;
import com.meetings.demo.entities.Meeting;
import com.meetings.demo.repos.DepartmentDB;
import com.meetings.demo.repos.EmployeeDB;
import com.meetings.demo.repos.MeetingDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.temporal.ChronoUnit.YEARS;

@Service
public class MainServiceImpl implements MainService {
    @Autowired
    private DepartmentDB departmentDB;
    @Autowired
    private EmployeeDB employeeDB;
    @Autowired
    private MeetingDB meetingDB;

    @Override
    public Iterable<Meeting> filterSearchMeeting(String theme, LocalDateTime fromDate, LocalDateTime toDate, Integer departmentId, Integer employeeId) {
        return meetingDB.findAll((Specification<Meeting>)(root, cq, cb) ->{
            Predicate p = cb.conjunction();
            System.out.println("theme =" + theme + " departmentName = " + departmentId + " participantName = " + employeeId);
            if(Objects.nonNull(theme)){
                p = cb.and(p, cb.like(root.get("theme"), "%" + theme + "%"));
            }
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.isBefore(toDate)) {
                p = cb.and(p, cb.between(root.get("time"), fromDate, toDate));
            }
            if(Objects.nonNull(departmentId)){
                p = cb.and(p, cb.equal(root.get("departmentId"), departmentId));
            }
            if (Objects.nonNull(employeeId)){
                p = cb.and(p, cb.equal(root.join("participantIds"), employeeId));
            }
            return p;
        });
    }

    @Override
    public Iterable<Meeting> findAllMeetings() {
        return meetingDB.findAll();
    }

    @Override
    public Iterable<Department> findAllDepartments() {
        return departmentDB.findAll();
    }

    @Override
    public Iterable<Employee> findAllEmployees() {
        return employeeDB.findAll();
    }

    @Override
    public void save(Meeting newMeeting) {
        meetingDB.save(newMeeting);
    }

    @Override
    public void save(Department newDepartment) {
        departmentDB.save(newDepartment);
    }

    @Override
    public void save(Employee newEmployee) {
        employeeDB.save(newEmployee);
    }

    @Override
    public Optional<Department> findByDepartmentName(String departmentName) {
        return departmentDB.findByName(departmentName);
    }

    @Override
    public Optional<Meeting> findByMeetingTheme(String meetingName) {
        return meetingDB.findByTheme(meetingName);
    }

    @Override
    public Optional<Meeting> findByMeetingId(int id) {
        return meetingDB.findById(id);
    }

    public MeetingDTO convertToDTO(Meeting meeting){
        MeetingDTO conversion = new MeetingDTO();
        conversion.setId(meeting.getId());
        conversion.setTheme(meeting.getTheme());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        conversion.setTime(meeting.getTime().format(formatter));
        conversion.setDepartment(convertToDTO(departmentDB.findById(meeting.getDepartmentId()).orElse(null)));
        Employee organizer = employeeDB.findById(meeting.getOrganizerId()).orElse(null);
        if (organizer!=null) conversion.setOrganizer(convertToDTO(organizer));
        List<String> participantNames = new ArrayList<>();
        List<EmployeeDTO> participants = new ArrayList<>();
        for (int participantId : meeting.getParticipantIds()){
            Employee participant = employeeDB.findById(participantId).orElse(null);
            if (participant!=null){
                participantNames.add(participant.getSurname() + " " + participant.getName() + " " + participant.getPatronymic());
                participants.add(convertToDTO(participant));
            }
        }
        conversion.setParticipantNames(participantNames);
        conversion.setParticipants(participants);
        conversion.setCount(participantNames.size());
        return conversion;
    }

    public EmployeeDTO convertToDTO(Employee employee){
        String surname = employee.getSurname();
        String name = employee.getName();
        String patronymic = employee.getPatronymic();
        EmployeeDTO conversion = new EmployeeDTO();
        conversion.setId(employee.getId());
        conversion.setName(surname + " " + name + " " + patronymic);
        conversion.setShortName(surname + " " + name.charAt(0) + '.' + patronymic.charAt(0));
        if (employee.getDepartment()!=null) conversion.setDepartment(employee.getDepartment().getName());
        conversion.setAge((int) YEARS.between(employee.getDateOfBirth(), LocalDate.now()));
        return conversion;
    }

    public DepartmentDTO convertToDTO(Department department){
        DepartmentDTO conversion = new DepartmentDTO();
        conversion.setId(department.getId());
        conversion.setName(department.getName());
        List<EmployeeDTO> departmentMembers = new ArrayList<>();
        for (Employee employee : department.getMembers()){
//            int id = employee.getId();
//            String fullName = employee.getSurname() + " " + employee.getName() + " " + employee.getPatronymic();
//            Person person = new Person();
//            person.setId(id);
//            person.setName(fullName);
            departmentMembers.add(convertToDTO(employee));
        }
        conversion.setMembers(departmentMembers);
        return conversion;
    }

//
//    @Override
//    public List<StudentDTO> getStudents(Date fromDate, Date toDate, String name, Pageable pageable) {
//        List<Students> students = studentRepository.findAll((Specification<Students>) (root, cq, cb) -> {
//            Predicate p = cb.conjunction();
//            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
//                p = cb.and(p, cb.between(root.get("createdDate"), fromDate, toDate));
//            }
//            if (!StringUtils.isEmpty(name)) {
//                p = cb.and(p, cb.like(root.get("name"), "%" + name + "%"));
//            }
//            cq.orderBy(cb.desc(root.get("name")), cb.asc(root.get("id")));
//            return p;
//        }, pageable).getContent();
//        return StudentConverter.convertToStudentDTO(students);
//    }
}
