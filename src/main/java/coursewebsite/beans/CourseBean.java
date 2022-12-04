package coursewebsite.beans;

import coursewebsite.Database.Database;
import coursewebsite.exceptions.AlreadyExistsException;
import coursewebsite.exceptions.DoesNotExistException;
import coursewebsite.models.Course;
import coursewebsite.models.Student;
import coursewebsite.models.Teacher;
import coursewebsite.models.User;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author IsmaTew
 */
@Named(value = "courseBean")
@SessionScoped
public class CourseBean implements Serializable {
    
    @PersistenceContext(unitName = "soar_PU")
    private EntityManager em;
    
    private String courseTitle = "";
    private double price = 0.0;

 protected boolean doesCourseExistInStudentCourses() {
        for (Course c : LoginBean.getStudentLoggedIn().getUserCourses()) {
            if (c.getTitle().equals(courseTitle)) {
                return true;
            }
            
        }
        return false;
    }
 
    public ArrayList<Course> getCourses() {
        return Database.getInstance().getCourses();
    }
 
    
    public ArrayList<Course> getStudentCourses() {
        return LoginBean.getStudentLoggedIn().getUserCourses();
    }
    
    public ArrayList<Course> getTeacherCourses() {
        return LoginBean.getTeacherLoggedIn().getUserCourses();
    }
    
    public boolean doesCourseExistInApp() throws DoesNotExistException {
        
        for (Course f : Database.getInstance().getCourses()) {
            if (f.getTitle().equals(courseTitle)) {
                return true;
            }
        }
        throw new DoesNotExistException("Course " + courseTitle + " does not exist.");
    }
    
    public boolean sameCourseTitle() throws AlreadyExistsException {
        for (Course f : Database.getInstance().getCourses()) {
            if (f.getTitle().equals(courseTitle)) {
                throw new AlreadyExistsException("Course " + courseTitle + " exists already.");
            }
        }
        return false;
        
    }
    
    public void deleteACourse(){
        Teacher t = LoginBean.getTeacherLoggedIn();
        if(searchCourse().getTeacher().equals(t)){
            t.deleteUserCourse(searchTeacherCourse());
            Database.getInstance().deleteCourse(searchTeacherCourse());
        }
        courseTitle = "";
    }
    
    public static Course findCourseByTitle(String t) throws DoesNotExistException{
        for (Course c : Database.getInstance().getCourses()) {
            if (c.getTitle().equals(t)) {
                return c;
            }
        }
        throw new DoesNotExistException("Course " + t + " does not exist.");
    }
    
    public Course searchCourse(){
        Student s = LoginBean.getStudentLoggedIn();
        try {
            Course f = findCourseByTitle(courseTitle);
            
            return f;
        } catch (DoesNotExistException ex) {
            System.out.println(ex.getMessage());
        }
        // empty values
        this.courseTitle = "";
        return null;
    
    }
    
    public Course searchTeacherCourse(){
        Teacher t = LoginBean.getTeacherLoggedIn();
        try {
            Course f = findCourseByTitle(courseTitle);
            
            return f;
        } catch (DoesNotExistException ex) {
            System.out.println(ex.getMessage());
        }
        // empty values
        this.courseTitle = "";
        return null;
    
    }

    public void createACourse() {
        try {
            if(!sameCourseTitle()){
                Course c = new Course(courseTitle, LoginBean.getTeacherLoggedIn(), price);
                Database.getInstance().addCourseInApp(new Course(courseTitle, LoginBean.getTeacherLoggedIn(), price));
                LoginBean.getTeacherLoggedIn().addUserCourse(c);
            }
        } catch (AlreadyExistsException ex) {
            System.out.println(ex.getMessage());
        }     

    }
    
    public double getPrice(){
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }

    public void setCourseTitle(String title) {
        this.courseTitle = title;
    }

    public String getCourseTitle() {
        return courseTitle;
    }
}
