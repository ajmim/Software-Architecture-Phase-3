package coursewebsite.beans;

import coursewebsite.Database.Database;
import coursewebsite.exceptions.DoesNotExistException;
import coursewebsite.models.Course;
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
    
    public boolean doesCourseExistInApp() {
        for (Course f : Database.getInstance().getCourses()) {
            if (f.getTitle().equals(courseTitle)) {
                return true;
            }
        }
        return false;
    }
    
    public void deleteACourse(Course c) throws DoesNotExistException{
        if(c.getTeacher().equals(LoginBean.getTeacherLoggedIn())){
            Database.getInstance().deleteCourse(c);
        }else{
            throw new DoesNotExistException("Course " + courseTitle + " does not exist.");
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

    public void createACourse(){
        if (!doesCourseExistInApp()){
            Database.getInstance().addCourseInApp(new Course(courseTitle, LoginBean.getTeacherLoggedIn(), price));
        }
    }

    public void setPrice(double p) {
        this.price = p;
    }

    public void setCourseTitle(String title) {
        this.courseTitle = title;
    }

    public String getCourseTitle() {
        return courseTitle;
    }
}
