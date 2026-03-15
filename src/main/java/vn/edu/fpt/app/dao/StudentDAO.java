    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package vn.edu.fpt.app.dao;

    import java.sql.Date;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.util.ArrayList;
    import java.util.List;

    import vn.edu.fpt.app.dao.DBContext;
    import vn.edu.fpt.app.dao.DepartmentDAO;
    import vn.edu.fpt.app.entities.Department;
    import vn.edu.fpt.app.entities.Student;

    /**
     *
     * @author Legion
     */
    public class StudentDAO extends DBContext {

        private DepartmentDAO departmentDAO = new DepartmentDAO();

        public List<Student> getAllStudent() {

            List<Student> list = new ArrayList<>();
            String sql = "SELECT * FROM Students";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {

                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    Date birthdate = rs.getDate("birthdate");
                    String gender = rs.getString("gender");
                    String address = rs.getString("address");
                    String city = rs.getString("city");

                    String code = rs.getString("departmentCode");
                    Department department = departmentDAO.getDepartmentByCode(code);

                    String email = rs.getString("Email");
                    String phone = rs.getString("Phone");
                    Student student = new Student(id, name, birthdate, gender, address, city, department, email, phone);

                    list.add(student);
                }
            } catch (Exception e) {
                System.out.println("Fail to get all students: " + e.getMessage());
            }
            return list;
        }

        public Boolean deleteStudentById(int id) {
            String sql = "DELETE FROM Students WHERE Students.id = ?";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                int num = ps.executeUpdate();
                if (num > 0) {
                    return true;
                }
            } catch (Exception e) {
                System.out.println("Fail to delete student by Id: " + e.getMessage());
            }
            return false;
        }

        public Boolean updateStudentByID(Student student) {
            String sql = "UPDATE [dbo].[Students] "
                    + "SET name = ?, "
                    + "    birthdate = ?, "
                    + "    gender = ?, "
                    + "    address = ?, "
                    + "    city = ?, "
                    + "    departmentCode = ?, "
                    + "     Email = ?, "
                    + "     Phone = ? "
                    + "WHERE id = ?";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, student.getName());
                ps.setDate(2, student.getBirthdate());
                ps.setString(3, student.getGender());
                ps.setString(4, student.getAddress());
                ps.setString(5, student.getCity());
                ps.setString(6, student.getDepartment().getCode());
                ps.setString(7, student.getEmail());
                ps.setString(8, student.getPhone());
                ps.setInt(9, student.getId());

                int num = ps.executeUpdate();

                if (num > 0) {
                    return true;
                }
            } catch (Exception e) {
                System.out.println("Fail to update student by ID: " + e.getMessage());
            }
            return false;
        }

        public Student getStudentById(int id) {
            String sql = "SELECT * FROM [Students] WHERE id = ?";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String name = rs.getString("name");
                    Date birthdate = rs.getDate("birthdate");
                    String gender = rs.getString("gender");
                    String address = rs.getString("address");
                    String city = rs.getString("city");
                    String code = rs.getString("departmentCode");

                    Department department = departmentDAO.getDepartmentByCode(code);

                    String email = rs.getString("Email");
                    String phone = rs.getString("Phone");

                    Student student = new Student(id, name, birthdate, gender, address, city, department, email, phone);
                    return student;
                }
            } catch (Exception e) {
                System.out.println("Fail to get student by ID: " + e.getMessage());

            }
            return null;
        }

        public Boolean insertNewStudent(Student student) {
            String sql = "INSERT INTO [dbo].[Students] "
                    + "           ([name] ,[birthdate] ,[gender] ,[address] ,[city] "
                    + "           ,[departmentCode] ,[Email] ,[Phone]) "
                    + "            VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, student.getName());
                ps.setDate(2, student.getBirthdate());
                ps.setString(3, student.getGender());
                ps.setString(4, student.getAddress());
                ps.setString(5, student.getCity());
                ps.setString(6, student.getDepartment().getCode());
                ps.setString(7, student.getEmail());
                ps.setString(8, student.getPhone());
                int row = ps.executeUpdate();
                if (row > 0) {
                    return true;
                }
            } catch (Exception e) {
                System.out.println("Fail to insert new student! " + e.getMessage());
            }
            return false;
        }

        public List<Student> FillterByname(String nameStudent) {

            List<Student> list = new ArrayList<>();
            String sql = "SELECT *\n"
                    + "FROM Students\n"
                    + "WHERE LOWER(name) LIKE LOWER(?);";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, "%" + nameStudent + "%");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {

                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    Date birthdate = rs.getDate("birthdate");
                    String gender = rs.getString("gender");
                    String address = rs.getString("address");
                    String city = rs.getString("city");

                    String code = rs.getString("departmentCode");
                    Department department = departmentDAO.getDepartmentByCode(code);

                    String email = rs.getString("Email");
                    String phone = rs.getString("Phone");
                    Student student = new Student(id, name, birthdate, gender, address, city, department, email, phone);

                    list.add(student);
                }
            } catch (Exception e) {
                System.out.println("Fail to get all students: " + e.getMessage());
            }
            return list;
        }

        public List<Student> FillterByDepartmetName(String DepartmetCode) {

            boolean filterByDept = DepartmetCode != null && !DepartmetCode.equalsIgnoreCase("all") && !DepartmetCode.isEmpty();

            String sql = "";
            if (filterByDept) {
                sql = "SELECT * FROM Students WHERE LOWER(departmentCode) LIKE LOWER(?)";
            } else {
                sql = "SELECT * FROM Students";
            }
            List<Student> list = new ArrayList<>();

            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                if (filterByDept) {
                    ps.setString(1, DepartmetCode);
                }
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {

                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    Date birthdate = rs.getDate("birthdate");
                    String gender = rs.getString("gender");
                    String address = rs.getString("address");
                    String city = rs.getString("city");

                    String code = rs.getString("departmentCode");
                    Department department = departmentDAO.getDepartmentByCode(code);

                    String email = rs.getString("Email");
                    String phone = rs.getString("Phone");
                    Student student = new Student(id, name, birthdate, gender, address, city, department, email, phone);

                    list.add(student);
                }
            } catch (Exception e) {
                System.out.println("Fail to get all students: " + e.getMessage());
            }
            return list;
        }

        public List<Student> FillterBoth(String DepartmetCode, String nameStudent) {

            List<Student> list = new ArrayList<>();
            String sql = "SELECT *\n"
                    + "FROM Students\n"
                    + "WHERE LOWER(departmentCode) LIKE LOWER(?) and LOWER(name) LIKE LOWER(?);";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, DepartmetCode);
                ps.setString(2, "%" + nameStudent + "%");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {

                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    Date birthdate = rs.getDate("birthdate");
                    String gender = rs.getString("gender");
                    String address = rs.getString("address");
                    String city = rs.getString("city");

                    String code = rs.getString("departmentCode");
                    Department department = departmentDAO.getDepartmentByCode(code);

                    String email = rs.getString("Email");
                    String phone = rs.getString("Phone");
                    Student student = new Student(id, name, birthdate, gender, address, city, department, email, phone);

                    list.add(student);
                }
            } catch (Exception e) {
                System.out.println("Fail to get all students: " + e.getMessage());
            }
            return list;
        }

        public static void main(String[] args) {
            StudentDAO dao = new StudentDAO();

            List<Student> list = dao.FillterBoth("SE","Ng");
//            for (Student student : list) {
//                System.out.println(student.toString());
//            }
//            DepartmentDAO depDAO = new DepartmentDAO();

            System.out.println(dao.deleteStudentById(2));
            String d = "2005-01-02";
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate b = LocalDate.parse(d, formatter);
    //        boolean c = a.update(new Student(8, "long dep trai", b, "nam", "333", "hn", new Department("IT", "")));
    //        System.out.println(c);
    //        String name = "Nguyen An Binh";
    //        Date birthdate = Date.valueOf("2006-06-11");
    //        String gender = "male";
    //        String addrss = "123 ÄÆ°á»ng 3/2";
    //        String city = "Cáº§n ThÆ¡";
    //        Department departmentCode = depDAO.getDepartmentByCode("SE");
    //        String email = "anbinh2006ct@gmail.com";
    //        String phone = "0812154005";
    //
    //        Student binh = new Student(0, name, birthdate, gender, addrss, city, departmentCode, email, phone);
    //        dao.insertNewStudent(binh);
    //
    //        Student student = dao.getStudentById(5);
    //        System.out.println(student);
    
    
    
        }

    }

