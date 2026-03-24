package vn.edu.fpt.app.service;

import vn.edu.fpt.app.entities.User;
import vn.edu.fpt.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** BÄƒm máº­t kháº©u báº±ng MD5 */
    public String hashMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(str.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean existsByUsername(String username) {
        return username != null && userRepository.existsByUsername(username.trim());
    }

    public boolean existsByLecturerId(Integer lecturerId) {
        return lecturerId != null && lecturerId > 0 && userRepository.existsByLecturer_Id(lecturerId);
    }

    /**
     * Kiá»ƒm tra Ä‘Äƒng nháº­p: so sÃ¡nh username vá»›i passwordHash (MD5).
     * @return User náº¿u há»£p lá»‡, null náº¿u sai
     */
    public User checkLogin(String username, String password) {
        String hashed = hashMD5(password);
        return userRepository.findByUsernameAndPassword(username, hashed).orElse(null);
    }

    @Transactional
    public boolean addNewUser(User user) {
        // Bam mat khau truoc khi luu
        user.setPassword(hashMD5(user.getPassword()));
        // Chi role lecturer/teacher moi duoc lien ket giang vien
        if (!("lecturer".equals(user.getRole()) || "teacher".equals(user.getRole()))) {
            user.setLecturer(null);
        }
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to add new user: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteUserById(int id) {
        if (!userRepository.existsById(id)) return false;
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to delete user: " + e.getMessage());
            return false;
        }
    }
}
