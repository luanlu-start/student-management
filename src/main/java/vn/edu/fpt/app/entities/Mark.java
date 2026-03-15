/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.fpt.app.entities;

import vn.edu.fpt.app.entities.Assessment;
import vn.edu.fpt.app.entities.Enrollment;
import vn.edu.fpt.app.entities.MarkId;
import jakarta.persistence.*;

/**
 * Composite PK: (enrollId, assessmentId) in Marks table.
 * Uses @IdClass with MarkId.
 *
 * @author Legion
 */
@Entity
@Table(name = "Marks")
@IdClass(MarkId.class)
public class Mark {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollId", nullable = false)
    private Enrollment enrollment;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessmentId", nullable = false)
    private vn.edu.fpt.app.entities.Assessment assessment;

    @Column(name = "mark", nullable = false)
    private double mark;

    public Mark() {
    }

    public Mark(Enrollment enrollment, vn.edu.fpt.app.entities.Assessment assessment, double mark) {
        this.enrollment = enrollment;
        this.assessment = assessment;
        this.mark = mark;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public vn.edu.fpt.app.entities.Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "Mark{" +
                "enrollment=" + enrollment +
                ", assessment=" + assessment +
                ", mark=" + mark +
                '}';
    }
}

