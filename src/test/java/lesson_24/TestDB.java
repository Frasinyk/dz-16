package lesson_24;

import lesson_24.models.Students;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TestDB {
    private Connection connection;

    private static final String SELECT = "SELECT * FROM students";
    private static final String SELECT_PARAMETRIZED = "SELECT * FROM students where id = ?";
    private static final String INSERT = "INSERT INTO students (first_name, last_name, grade, phone_number) VALUES (?,?,?,?)";
    private static final String UPDATE = "update students set first_name =? where id=?";
    private static final String DELETE = "delete from students where id=?";

    @BeforeMethod
    public void setup() {
        try {
            this.connection = DataBaseManager.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test(priority = 1)
    public void getStudents() {
        this.showStudents();
    }

    public void showStudents() {
        List<Students> students = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(SELECT);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Students student = new Students(resultSet.getInt("id"), resultSet.getString("first_name"),
                        resultSet.getString("last_name"), resultSet.getInt("grade"), resultSet.getString("phone_number"));
                students.add(student);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println("S count => " + students.size());
        students.forEach(e -> {
            System.out.println(e);
        });
    }

    @Test(priority = 2)
    public void showStudentByID() {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(SELECT_PARAMETRIZED);
            preparedStatement.setInt(1, 30);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Students student = new Students(resultSet.getInt("id"), resultSet.getString("first_name"),
                        resultSet.getString("last_name"), resultSet.getInt("grade"), resultSet.getString("phone_number"));
                System.out.println(student);
            }
            else{
                System.out.println("Student not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test(priority = 3)
    public void addNewStudent() {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(INSERT);
            preparedStatement.setString(1, "Vova");
            preparedStatement.setString(2, "ziLvova");
            preparedStatement.setInt(3, 1);
            preparedStatement.setString(4, "8097225566");
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        this.showStudents();
    }

    @Test(priority = 4)
    public void editStudent() {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, "Slavik");
            preparedStatement.setInt(2, 31);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        this.showStudents();
    }

    @Test(priority = 5)
    public void deleteStudent() {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(DELETE);
            preparedStatement.setInt(1, 30);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        this.showStudents();
    }

}
