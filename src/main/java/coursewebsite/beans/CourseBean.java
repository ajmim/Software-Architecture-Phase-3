package coursewebsite.beans;

import coursewebsite.models.Course;
import coursewebsite.models.User;
import coursewebsite.client.PersistenceClient;
import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;



@Named(value = "courseBean")
@SessionScoped
public class CourseBean implements Serializable {
    
    private String courseTitle = "";
    private double price = 0.0;
 
 
    public ArrayList<Course> getCourses() {
        return new ArrayList(PersistenceClient.getInstance().getAllCourses());
    }

    /*public Course findCourseByTitle() throws DoesNotExistException {
        return PersistenceClient.getInstance().getCourseByTitle(courseTitle);
    }*/
    
    public Course searchCourse(){
        return PersistenceClient.getInstance().getCourseByTitle(courseTitle);
    }
    
    public Boolean doesCourseExist() {
        Course c = searchCourse();
        if(c != null){return true;}
        return false;
    }
    
    public Boolean doesCourseNotExist() {
        Course c = searchCourse();
        if(c == null){return true;}
        return false;
    }
    
    //@Transactional
    public void createACourse() { 
      
            if(!doesCourseExist()){
                Course newCourse = new Course();
                newCourse.setTitle(courseTitle);
                newCourse.setPrice(price);
                newCourse.setFkTeacherId(LoginBean.getUserLoggedIn());
                PersistenceClient.getInstance().createCourse(newCourse);
            }
    
    }
    
    //@Transactional
    public void deleteACourse()  {      
            User t = LoginBean.getUserLoggedIn();
            Course c = searchCourse();
            if (doesCourseNotExist()) {
                System.out.print("----------------TEST 1-------------");
                return;
            }
            //if(c.getFkTeacherId().equals(t)){
            if(t != null){
                System.out.print("----------------TEST 2-------------");

                PersistenceClient.getInstance().removeCourse(c.getCourseId());
            }
            courseTitle = "";
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
