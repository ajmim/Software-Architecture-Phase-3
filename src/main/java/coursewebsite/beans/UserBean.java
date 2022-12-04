package coursewebsite.beans;

import coursewebsite.Database.Database;
import coursewebsite.exceptions.AlreadyExistsException;
import coursewebsite.exceptions.DoesNotExistException;
import coursewebsite.exceptions.InsufficientBalanceException;
import coursewebsite.models.Course;
import coursewebsite.models.Student;
import coursewebsite.models.Teacher;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import coursewebsite.beans.LoginBean;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Melike Geçer
 */
@Named(value = "userBean")
@SessionScoped
public class UserBean implements Serializable {
    @PersistenceContext(unitName = "soar_PU")
    private EntityManager em;
    
    private String email = "";
    private String username = "";
    private String firstName = "";
    private String lastName = "";
    private String password = "";
    private double amount = 0.0;

    public void createAStudent() throws AlreadyExistsException, DoesNotExistException {
        if (!emailStudentExists() && !usernameStudentExists()) {
            Student s = new Student();
            s.setUsername(username);
            s.setFirstName(firstName);
            s.setLastName(lastName);
            s.setEmail(email);
            s.setPassword(password.hashCode());
            em.persist(s);
            //Database.getInstance().addAStudent(new Student(username, firstName, lastName, email, password));
        } else {throw new AlreadyExistsException("This username already exist");}
        // empty values
        this.email = "";
        this.username = "";
        this.firstName = "";
        this.lastName = "";
        this.password = "";
        }
    
    public void createATeacher() throws AlreadyExistsException, DoesNotExistException{
        if (!emailTeacherExists() && !usernameTeacherExists()) {
            Teachet t = new Student();
            t.setUsername(username);
            t.setFirstName(firstName);
            t.setLastName(lastName);
            t.setEmail(email);
            t.setPassword(password.hashCode());
            em.persist(t);
        } else {throw new AlreadyExistsException("This username already exist");}
        // empty values
        this.email = "";
        this.username = "";
        this.firstName = "";
        this.lastName = "";
        this.password = "";
    }

    public void increaseBalance() {
        LoginBean.getStudentLoggedIn().increaseBalance(amount);
        this.amount = 0.0;
    }

    public void completeEnroll(Course course) throws InsufficientBalanceException, AlreadyExistsException {
        try {
            System.out.print("test here");
            Course c = doesCourseExistInUserCourses(course);
            LoginBean.getStudentLoggedIn().enroll(c);
        } catch (InsufficientBalanceException ex) {
            System.out.println(ex.getMessage());
        } catch (AlreadyExistsException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    
    public Course doesCourseExistInUserCourses(Course course) throws AlreadyExistsException{
        for (Course c : LoginBean.getStudentLoggedIn().getUserCourses()) {
                if (course.equals(c)) {
                throw new AlreadyExistsException("This course is already in your list of courses.");
                }
            }
        return course;

    }

    protected static Student findStudentByUsername(String username) throws DoesNotExistException {
        for (Student student : Database.getInstance().getStudents()) {
            if (student.getUsername().equals(username)) {
                return student;
            }
        }
        throw new DoesNotExistException("The student " + username + " does not exist.");
    }

        
    protected static Teacher findTeacherByUsername(String username) throws DoesNotExistException {
        for (Teacher teacher : Database.getInstance().getTeachers()) {
            if (teacher.getUsername().equals(username)) {
                return teacher;
            }
        }
        throw new DoesNotExistException("The teacher " + username + " does not exist.");
    }   
        
        
    private boolean emailStudentExists() throws AlreadyExistsException {
        for (Student student : Database.getInstance().getStudents()) {
            if (student.getEmail().equals(email)) {
                throw new AlreadyExistsException("The email " + email + " already in use.");
            }
        }
        return false;
    }
        
    private boolean emailTeacherExists() throws AlreadyExistsException {
        for (Teacher teacher : Database.getInstance().getTeachers()) {
            if (teacher.getEmail().equals(email))  {
                throw new AlreadyExistsException("The email " + email + " already in use.");
            }
        }
        return false;
    }    

    protected boolean usernameStudentExists() throws DoesNotExistException {
        for (Student student : Database.getInstance().getStudents()) {
            if (student.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    protected boolean usernameTeacherExists() throws DoesNotExistException {
        for (Teacher teacher : Database.getInstance().getTeachers()) {
            if (teacher.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    public double getAmount() {
        return amount;
    }

    public double getBalance() {
        return amount;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
        
        
    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public ArrayList<Teacher> getAllTeachers() {
        return Database.getInstance().getTeachers();
    }
    
    public ArrayList<Transaction> getStudentTransactions() {
        return LoginBean.getStudentLoggedIn().getTransactions();
    }
    public ArrayList<Transaction> getTeacherTransactions() {
        return LoginBean.getTeacherLoggedIn().getTransactions();
    }
}
