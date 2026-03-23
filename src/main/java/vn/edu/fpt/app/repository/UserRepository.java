package vn.edu.fpt.app.repository;

import vn.edu.fpt.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByLecturer_Id(Integer lecturerId);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
