package coursewebsite.beans;

import coursewebsite.Database.Database;
import coursewebsite.exceptions.DoesNotExistException;
import coursewebsite.models.Course;
import coursewebsite.models.Student;
import coursewebsite.models.User;
import java.util.ArrayList;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author IsmaTew
 */
@Named(value = "courseBean")
@SessionScoped
public class CourseBean implements Serializable {

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
    
    public void deleteACourse(String title) throws DoesNotExistException{
        try{
            courseTitle = title;
            if(findCourseByTitle(title).getTeacher().equals(LoginBean.getTeacherLoggedIn())){
                Database.getInstance().deleteCourse(findCourseByTitle(title));
            }
        }catch(DoesNotExistException ex) {
            System.out.println(ex.getMessage());
        }
        
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

    public void createACourse() throws DoesNotExistException{
        try{
            if (!doesCourseExistInApp()){
                Database.getInstance().addCourseInApp(new Course(courseTitle, LoginBean.getTeacherLoggedIn(), price));
            }
        }catch(DoesNotExistException ex) {
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
