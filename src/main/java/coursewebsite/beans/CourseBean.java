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
    public boolean doesCourseExistInApp() {
        for (Course f : Database.getInstance().getCourses()) {
            if (f.getTitle().equals(courseTitle)) {
                return true;
            }
        }
        return false;
    }
    public static void deleteACourse(Course c){
        if(c.getTeacher().equals(LoginBean.getTeacherLoggedIn())){
            Database.getInstance().deleteCourse(c);
            System.out.println("Deleted successfully.");
        }else{
            System.out.println("You are not the owner of this course, you can't delete it.");
        }
    }
    public static Course findCourseByTitle(String course) throws DoesNotExistException{
        for (Course c : Database.getInstance().getCourses()) {
            if (c.getTitle().equals(course)) {
                return c;
            }
        }
        throw new DoesNotExistException("Course " + course + " does not exist.");
    }

    public void createACourse(){
        if (!doesCourseExistInApp()){
            Database.getInstance().addCourseInApp(new Course(courseTitle, LoginBean.getTeacherLoggedIn(), price));
        }
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseTitle() {
        return courseTitle;
    }
}
