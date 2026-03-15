package vn.edu.fpt.app.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite Primary Key class for Mark entity.
 * Maps to (enrollId, assessmentId) in the Marks table.
 *
 * @author Legion
 */
public class MarkId implements Serializable {

    private int enrollment;   // matches field name in Mark.enrollment
    private int assessment;   // matches field name in Mark.assessment

    public MarkId() {
    }

    public MarkId(int enrollment, int assessment) {
        this.enrollment = enrollment;
        this.assessment = assessment;
    }

    public int getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(int enrollment) {
        this.enrollment = enrollment;
    }

    public int getAssessment() {
        return assessment;
    }

    public void setAssessment(int assessment) {
        this.assessment = assessment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarkId)) return false;
        MarkId markId = (MarkId) o;
        return enrollment == markId.enrollment && assessment == markId.assessment;
    }

    @Override
    public int hashCode() {
        return Objects.hash(enrollment, assessment);
    }
}

