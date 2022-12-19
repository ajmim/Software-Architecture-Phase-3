package coursewebsite.beans;

import coursewebsite.exceptions.AlreadyExistsException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import coursewebsite.beans.LoginBean;
import coursewebsite.client.PersistenceClient;
import coursewebsite.exceptions.InsufficientBalanceException;
import coursewebsite.models.Course;
import coursewebsite.models.Transaction;
import coursewebsite.models.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Named(value = "userBean")
@SessionScoped
public class UserBean implements Serializable {
    
    private String email = "";
    private String username = "";
    private String firstName = "";
    private String lastName = "";
    private String password = "";
    private double amount = 0.0;
    private String category = "";
    

    //@Transactional
    public void createAStudent() throws AlreadyExistsException {
        try{
            boolean a = !PersistenceClient.getInstance().emailExists(email);
            boolean b = PersistenceClient.getInstance().getUsersByName(username) == null;
            if (a && b) {        
                User newStudent = new User();
                newStudent.setUsername(username);
                newStudent.setFirstname(firstName);
                newStudent.setLastname(lastName);
                newStudent.setEmail(email);
                newStudent.setPassword(password);
                newStudent.setCategory("student");
                PersistenceClient.getInstance().createUser(newStudent);
            } 
        } catch(AlreadyExistsException ex){
            System.out.println(ex.getMessage());
        }
        // empty values
        this.email = "";
        this.username = "";
        this.firstName = "";
        this.lastName = "";
        this.password = "";
        }
    
    //@Transactional
    public void createATeacher() throws AlreadyExistsException{
        try{
            boolean a = !PersistenceClient.getInstance().emailExists(email);
            boolean b = PersistenceClient.getInstance().getUsersByName(username) == null;
            if (a && b) {
                User t = new User();
                t.setUsername(username);
                t.setFirstname(firstName);
                t.setLastname(lastName);
                t.setEmail(email);
                t.setPassword(password);
                t.setCategory("teacher");
                PersistenceClient.getInstance().createUser(t);
            }
        } catch(AlreadyExistsException ex){
            System.out.println(ex.getMessage());
        }
        // empty values
        this.email = "";
        this.username = "";
        this.firstName = "";
        this.lastName = "";
        this.password = "";
    }
    
    //@Transactional
    public void increaseBalance() {
        User s = LoginBean.getUserLoggedIn();
        s.setBalance(s.getBalance() + amount);
        PersistenceClient.getInstance().updateUser(s);
        // empty value
        this.amount = 0.0;
    }
    
    //@Transactional
    public void enroll(Course c){ //throws InsufficientBalanceException, AlreadyExistsException 
        User s = LoginBean.getUserLoggedIn();
        User t = c.getFkTeacherId();
        
        //List<Course> userCourses = PersistenceClient.getInstance().getStudentCourses(s.getUserId());
        List<User> tmp = PersistenceClient.getInstance().getEnrolledStudents(c.getCourseId());
        //List<User> enrolledStudents = PersistenceClient.getInstance().getEnrolledStudents(c.getCourseId());
        
        /*
        if(s.getBalance() < c.getPrice()){
            throw new InsufficientBalanceException("you don't have enough money in your account.");
        }if(userCourses.contains(c)){
            throw new AlreadyExistsException("You are already enrolled in this course.");
        }
        */
        
        if(s.getBalance() > c.getPrice() && !tmp.contains(s)){
            s.setBalance(s.getBalance() - c.getPrice());
            t.setBalance(t.getBalance() + c.getPrice());
            // ADDING TO THE REL. TABLE
            tmp.add(s);
            c.setUserCollection(tmp);
            
            //--------------------ADD COURSE UPDATE -------------------------
            
            PersistenceClient.getInstance().updateCourse(c);
            PersistenceClient.getInstance().updateUser(s);
            PersistenceClient.getInstance().updateUser(t);
        }
        
    }
    
    //@Transactional
    public void completeEnroll(Course course) throws InsufficientBalanceException, AlreadyExistsException  {
        //try{
            enroll(course);
        //}catch(InsufficientBalanceException | AlreadyExistsException ex){
        //    System.out.println(ex.getMessage());
        //}
    }
    
    // MOMO : I am removing it because we added it in Client file
    //
    /*private boolean emailExists() throws AlreadyExistsException {
        Query query = em.createQuery("SELECT u.email FROM User u WHERE u.email = :email").setParameter("email", email);
        List<User> users = query.getResultList();
        if(users.size() > 0){throw new AlreadyExistsException("Email already exists.");}
        return false;
    }
          
    protected boolean usernameExists() throws AlreadyExistsException { 
        Query query = em.createQuery("SELECT u.username FROM User u WHERE u.username = :username").setParameter("username", username);
        List<User> users = query.getResultList();
        if(users.size() > 0){throw new AlreadyExistsException("username already exists.");}
        return false;
    }*/
    
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
    
    public Collection<Course> getStudentCourses(){
        int user_id = LoginBean.getUserLoggedIn().getUserId();
        return PersistenceClient.getInstance().getStudentCourses(user_id);
    }
    
    public List<Course> getTeacherCourses(){
        int user_id = LoginBean.getUserLoggedIn().getUserId();
        return PersistenceClient.getInstance().getTeacherCourses(user_id);
    }
    
    
    public List<User> getAllTeachers() {
return PersistenceClient.getInstance().getAllTeachers();
    }
    
    public ArrayList<Transaction> getStudentTransactions() {
        User s = LoginBean.getUserLoggedIn();
        int currentStudentId = s.getUserId();
        ArrayList<Transaction> transactions = new ArrayList<>();
        
        for (Course c : PersistenceClient.getInstance().getStudentCourses(currentStudentId)){
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
        ArrayList<Course> courses = new ArrayList(PersistenceClient.getInstance().getAllCourses());

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
