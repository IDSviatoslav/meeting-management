package com.meetings.demo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "meetings")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    String theme;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime time;
    @NotNull
    int departmentId;
    @NotNull
    int organizerId;

    @ElementCollection
    private Collection<Integer> participantIds = new ArrayList<>();

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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
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

    public Collection<Integer> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(Collection<Integer> participantIds) {
        this.participantIds = participantIds;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "id=" + id +
                ", theme='" + theme + '\'' +
                ", time=" + time +
                ", departmentId=" + departmentId +
                ", organizerId=" + organizerId +
                ", participants=" + participantIds +
                '}';
    }
}
