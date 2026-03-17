# Authorization Mapping - Role-Based Access Control

## Role Definitions
- **admin**: Full system access (manage departments, users, semesters, courses, lecturers)
- **academic_staff**: Academic management (manage students, classes, enrollments, assessments, marks)
- **lecturer**: View/manage grades for their own classes

## Controller Authorization Map

### 1. StudentController
- `list()` → `hasAnyRole('admin', 'academic_staff')`
- `add/edit/delete/view()` → `hasAnyRole('admin', 'academic_staff')`
- **Status**: ✅ COMPLETED

### 2. MarkController  
- All endpoints → `hasAnyRole('admin', 'academic_staff', 'lecturer')`
- **Status**: ✅ COMPLETED

### 3. CourseController
- All endpoints → `hasRole('admin')`
- **Status**: ✅ COMPLETED

### 4. ClassController
- `list/add/edit/delete/view()` → `hasAnyRole('admin', 'academic_staff')`
- **Status**: ⏳ IN PROGRESS

### 5. DepartmentController
- All endpoints → `hasRole('admin')`
- **Status**: ⏳ TODO

### 6. EnrollmentController
- All endpoints → `hasAnyRole('admin', 'academic_staff')`
- **Status**: ⏳ TODO

### 7. AssessmentController
- All endpoints → `hasAnyRole('admin', 'academic_staff')`
- **Status**: ⏳ TODO

### 8. LecturerController
- All endpoints → `hasRole('admin')`
- **Status**: ⏳ TODO

### 9. SemesterController
- All endpoints → `hasRole('admin')`
- **Status**: ⏳ TODO

### 10. UserController
- All endpoints → `hasRole('admin')`
- **Status**: ⏳ TODO

## Notes
- All endpoints require `@PreAuthorize` annotation
- Import: `import org.springframework.security.access.prepost.PreAuthorize;`
- Roles format: `ROLE_admin`, `ROLE_academic_staff`, `ROLE_lecturer`

