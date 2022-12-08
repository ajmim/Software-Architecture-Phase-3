package coursewebsite.beans;

import coursewebsite.exceptions.AlreadyExistsException;
import coursewebsite.exceptions.DoesNotExistException;
import coursewebsite.exceptions.InsufficientBalanceException;
import coursewebsite.models.Course;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import coursewebsite.beans.LoginBean;
import coursewebsite.models.Transaction;
import coursewebsite.models.User;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author Melike Geçer
 */
@Named(value = "userBean")
@SessionScoped
//@SuppressWarnings("unchecked")
public class UserBean implements Serializable {
    
    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;
    
    private String email = "";
    private String username = "";
    private String firstName = "";
    private String lastName = "";
    private String password = "";
    private double amount = 0.0;
    private String category = "";
    

    @Transactional
    public void createAStudent() throws AlreadyExistsException, DoesNotExistException {
        //try{
            if (!emailExists() && !usernameExists()) {        
                User newStudent = new User();
                newStudent.setUsername(username);
                newStudent.setFirstname(firstName);
                newStudent.setLastname(lastName);
                newStudent.setEmail(email);
                newStudent.setPassword(password);
                newStudent.setCategory("student");
                em.persist(newStudent);
            } 
        //} catch(AlreadyExistsException| DoesNotExistException ex){
        //    System.out.println(ex.getMessage());
        //}
        // empty values
        this.email = "";
        this.username = "";
        this.firstName = "";
        this.lastName = "";
        this.password = "";
        }
    
    public void createATeacher() throws AlreadyExistsException, DoesNotExistException{
        if (!emailExists() && !usernameExists()) {
            User t = new User();
            t.setUsername(username);
            t.setFirstname(firstName);
            t.setLastname(lastName);
            t.setEmail(email);
            t.setPassword(password);
            t.setCategory("teacher");
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
        //LoginBean.getStudentLoggedIn().increaseBalance(amount);
        //this.amount = 0.0;
        
        User s = LoginBean.getUserLoggedIn();
        s.setBalance(s.getBalance() + amount);
        em.merge(s);
        // empty value
        this.amount = 0.0;
    }
    
    //TO DO
    /*
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
    */
    
    
    private boolean emailExists() { //throws AlreadyExistsException
        Query query = em.createQuery("SELECT u.email FROM User u WHERE u.email = :email").setParameter("email", email);
        List<User> users = query.getResultList();
        return users.size() > 0;
    }
          
    
    protected boolean usernameExists() { //throws DoesNotExistException
        Query query = em.createQuery("SELECT u.username FROM User u WHERE u.username = :username").setParameter("username", username);
        List<User> users = query.getResultList();
        return users.size() > 0;
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
    
    public List<User> getAllTeachers() {
        Query q = em.createNamedQuery("User.findByCategory", User.class).setParameter("category", category);
        List<User> teachers = q.getResultList();
        return teachers;
    }
    
    /*public ArrayList<Transaction> getStudentTransactions() {
        //defining variables
        Student s = LoginBean.getStudentLoggedIn();
        int currentStudentId = s.getFkUserStudentId();
        ArrayList<Transaction> transactions = new ArrayList<>();
        
        //selecting the enrolled table with currentStudent only --> get_courses
        Query q1 = em.createQuery(
                "SELECT fk_course_student_id FROM enrolled e "
                        + "WHERE e.fk_fk_user_student_id = :currentStudentId"
        );
        List<Integer> courseIds = q1.getResultList(); //list of the courses ids 
        
        //For each course, create a transaction using currentStudent, teacher of course, and price of course      
        for(int i : courseIds){
            // 1)Get Teacher entity
            Query q2 = em.createQuery("SELECT fk_fk_user_teacher_id "
                    + "FROM responsible_for f WHERE f.course_teacher_id = " + i
            );
            List<Integer> tId = q2.getResultList();
            int teacherId = tId.get(0);
            Query q3 = em.createQuery("SELECT u from user u WHERE u.user_id = :teacher id");
            List<Teacher> tmp_t = q3.getResultList();
            Teacher t = tmp_t.get(0);

            // 2) get Course Price
            Query q4 = em.createQuery("SELECT c.price FROM course c WHERE c.course_id = " + i);

            List<Integer> coursePrice = q4.getResultList();
            double p = coursePrice.get(0);

            //Creating Transaction and adding it to the arraylist
            Transaction tr = Transaction.createTransaction(s, t, p);
            transactions.add(tr);
    
        }
        return transactions;
    }
    
    public ArrayList<Transaction> getTeacherTransactions() {
        //defining variables
        Teacher t = LoginBean.getTeacherLoggedIn();
        ArrayList<Transaction> transactions = new ArrayList<>();
        int currentTeacherId = t.getFkUserTeacherId();

        //selecting the responsible table with right courses --> get_courses
        Query q1 = em.createQuery(
                "SELECT fk_course_teacher_id FROM responsible_for r "
                        + "WHERE r.fk_fk_user_teacher_id = :currentTeacherId"
        );
        List<Integer> courseIds = q1.getResultList(); //list of the courses ids 
        
        for(int i : courseIds){
            // 1)Get Course Price
            Query q = em.createQuery("SELECT course.price FROM course WHERE course.course_id = " + i);
            List<Integer> tmp_p = q.getResultList();
            int p = tmp_p.get(0);
            
            //Get student
            Query q2 = em.createQuery("SELECT fk_fk_user_student_id "
                    + "FROM enrolled e WHERE e.course_student_id = " + i
            );
            List<Integer> studentIds = q2.getResultList();
            for(int j:studentIds){
                Query qq = em.createQuery("SELECT u FROM user u WHERE u.user_id = " + j);
                List<Student> s_tmp = qq.getResultList();
                Student s = s_tmp.get(0);
                
                Transaction tr = Transaction.createTransaction(s, t, p);
                transactions.add(tr);
                
            }
        }
        return transactions;
    }*/
}
