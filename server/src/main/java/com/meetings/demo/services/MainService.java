package com.meetings.demo.services;

import com.meetings.demo.entities.Meeting;

import java.util.Date;

public interface MainService {
    Iterable<Meeting> filterSearchMeeting(String theme, Date fromDate, Date toDate, Integer departmentId, Integer participantName);
}
