package coursewebsite.beans;

import coursewebsite.exceptions.AlreadyExistsException;
import coursewebsite.exceptions.DoesNotExistException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import coursewebsite.beans.LoginBean;
import coursewebsite.beans.CourseBean;
import coursewebsite.exceptions.InsufficientBalanceException;
import coursewebsite.models.Course;
import coursewebsite.models.Transaction;
import coursewebsite.models.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Named(value = "userBean")
@SessionScoped
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
    
    @Transactional
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
    
    @Transactional
    public void increaseBalance() {
        //LoginBean.getStudentLoggedIn().increaseBalance(amount);
        //this.amount = 0.0;
        
        User s = LoginBean.getUserLoggedIn();
        s.setBalance(s.getBalance() + amount);
        em.merge(s);
        // empty value
        this.amount = 0.0;
    }
    
    @Transactional
    public void enroll(Course c) { //throws InsufficientBalanceException, AlreadyExistsException
        User s = LoginBean.getUserLoggedIn();
        User t = c.getFkTeacherId();
        Collection<Course> userCourses = s.getCourseCollection();

        if(s.getBalance() > c.getPrice() && !userCourses.contains(c)){
            s.setBalance(s.getBalance() - c.getPrice());
            t.setBalance(t.getBalance() + c.getPrice());
            // ADDING TO THE REL. TABLE
            Collection<User> tmp = c.getUserCollection();
            tmp.add(s);
            em.merge(c);
        }
        
    }
    
    @Transactional
    public void completeEnroll(Course course)  {//throws InsufficientBalanceException, AlreadyExistsException
        enroll(course);
    }
    
    
    
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
    
    public List<Course> getStudentCourses(){
        int user_id = LoginBean.getUserLoggedIn().getUserId();

        Query q = em.createQuery("SELECT c FROM Course c INNER JOIN c.userCollection u WHERE u.userId = :user_id"
                , Course.class).setParameter("user_id", user_id);
        
        return q.getResultList();
    }
    
    public ArrayList<Course> getTeacherCourses(){
        User t = LoginBean.getUserLoggedIn();
        //int currentStudentId = t.getUserId();
        ArrayList<Course> courses = new ArrayList(em.createNamedQuery("Course.findAll", Course.class).getResultList());
        ArrayList<Course> t_courses = new ArrayList();
        for(Course c : courses){
            if(c.getFkTeacherId().equals(t)){
                t_courses.add(c);
                }
            }
        return t_courses;
    }
    
    public List<User> getAllTeachers() {
        Query q = em.createNamedQuery("User.findByCategory", User.class).setParameter("category", "teacher");
        List<User> teachers = q.getResultList();
        return teachers;
    }
    
    public ArrayList<Transaction> getStudentTransactions() {
        //defining variables
        User s = LoginBean.getUserLoggedIn();
        int currentStudentId = s.getUserId();
        ArrayList<Transaction> transactions = new ArrayList<>();
        
        for (Course c : getStudentCourses()){
            User teacher_id = c.getFkTeacherId();
            double c_price = c.getPrice();
            transactions.add(Transaction.createTransaction(s, teacher_id, c_price));
            
        }
       
        return transactions;
    }
    
    
    public ArrayList<Transaction> getTeacherTransactions() {
        User t = LoginBean.getUserLoggedIn();
        //int currentStudentId = t.getUserId();
        ArrayList<Transaction> transactions = new ArrayList<>();
        ArrayList<Course> courses = new ArrayList(em.createNamedQuery("Course.findAll", Course.class).getResultList());
        
        
        for(Course c : courses){
            if(c.getFkTeacherId().equals(t)){
                double price = c.getPrice();
                Collection<User> enrolled = c.getUserCollection();
                for(User s : enrolled){
                    transactions.add(Transaction.createTransaction(s, t, price));
                }
            }
        }

        return transactions;
    }
}
