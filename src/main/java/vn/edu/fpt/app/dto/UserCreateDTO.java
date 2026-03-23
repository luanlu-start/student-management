package vn.edu.fpt.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserCreateDTO {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50, message = "Username must be 4-50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username contains invalid characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be 6-100 characters")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    @NotBlank(message = "Role is required")
    private String role;

    @NotNull(message = "Please select lecturer (or N/A)")
    private Integer lecturerId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(Integer lecturerId) {
        this.lecturerId = lecturerId;
    }
}

