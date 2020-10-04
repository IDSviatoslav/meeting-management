package com.meetings.demo.repos;

import com.meetings.demo.entities.Meeting;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MeetingDB extends CrudRepository<Meeting, Integer>, JpaSpecificationExecutor<Meeting> {
    Optional<Meeting> findByTheme(String name);
    List<Meeting> findAllByTime(LocalDateTime time);
    Iterable<Meeting> findAllByOrderByTimeAsc();
}
