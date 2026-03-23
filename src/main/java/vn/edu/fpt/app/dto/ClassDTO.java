package vn.edu.fpt.app.dto;

import jakarta.validation.constraints.*;

public class ClassDTO {

    private Integer classId;

    @NotNull(message = "Course is required")
    private Integer courseId;

    @NotNull(message = "Lecturer is required")
    private Integer lecturerId;

    @NotNull(message = "Semester is required")
    private Integer semesterId;

    @NotBlank(message = "Class code is required")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    private String code;

    @NotBlank(message = "Room is required")
    @Size(max = 50, message = "Room must not exceed 50 characters")
    private String room;

    @NotBlank(message = "Schedule is required")
    @Size(max = 100, message = "Schedule must not exceed 100 characters")
    private String schedule;
    private String courseTitle;

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    // getters & setters
    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(Integer lecturerId) {
        this.lecturerId = lecturerId;
    }

    public Integer getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
