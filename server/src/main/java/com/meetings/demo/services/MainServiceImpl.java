package com.meetings.demo.services;

import com.meetings.demo.entities.Department;
import com.meetings.demo.entities.Meeting;
import com.meetings.demo.repos.DepartmentDB;
import com.meetings.demo.repos.EmployeeDB;
import com.meetings.demo.repos.MeetingDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class MainServiceImpl implements MainService {
    @Autowired
    private DepartmentDB departmentDB;
    @Autowired
    private EmployeeDB employeeDB;
    @Autowired
    private MeetingDB meetingDB;

    @Autowired
    EntityManager entityManager;

    Optional<Department> findByDepartmentName(String name){
        return departmentDB.findByName(name);
    }

    Optional<Meeting> findByMeetingTheme(String name){
        return meetingDB.findByTheme(name);
    }

    void save(Department newDepartment){

    }

    @Override
    public Iterable<Meeting> filterSearchMeeting(String theme, Date fromDate, Date toDate, Integer departmentId, Integer employeeId) {
        return meetingDB.findAll((Specification<Meeting>)(root, cq, cb) ->{
            Predicate p = cb.conjunction();
            System.out.println("theme =" + theme + " departmentName = " + departmentId + " participantName = " + employeeId);
            if(Objects.nonNull(theme)){
                p = cb.and(p, cb.like(root.get("theme"), "%" + theme + "%"));
            }
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
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

//    public List<Employee> findByCriteria(String employeeName){
//        return employeeDAO.findAll(new Specification<Employee>() {
//            @Override
//            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                List<Predicate> predicates = new ArrayList<>();
//                if(employeeName!=null) {
//                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("employeeName"), employeeName)));
//                }
//                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
//            }
//        });
//    }
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
