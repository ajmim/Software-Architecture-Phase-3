package coursewebsite.beans;

import coursewebsite.exceptions.AlreadyExistsException;
import coursewebsite.exceptions.DoesNotExistException;
import coursewebsite.models.Course;
import coursewebsite.models.User;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Named(value = "courseBean")
@SessionScoped
//@SuppressWarnings("unchecked")
//@transactionnal 
public class CourseBean implements Serializable {
   
    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;
    
    private String courseTitle = "";
    private double price = 0.0;
 
 /*protected boolean doesCourseExistInStudentCourses() {
        for (Course c : LoginBean.getStudentLoggedIn().getUserCourses()) {
            if (c.getTitle().equals(courseTitle)) {
                return true;
            }
            
        }
        return false;
    }*/

    
    
    public ArrayList<Course> getCourses() {
        return new ArrayList(em.createNamedQuery("Course.findAll", Course.class).getResultList());
    }
    
 
    
    /*public ArrayList<Course> getStudentCourses() {
        return LoginBean.getStudentLoggedIn().getUserCourses();
    }*/
    
    /*public ArrayList<Course> getTeacherCourses() {
        return LoginBean.getTeacherLoggedIn().getUserCourses();
    }*/
    
    /*public boolean doesCourseExistInApp() throws DoesNotExistException {
        
        for (Course f : Database.getInstance().getCourses()) {
            if (f.getTitle().equals(courseTitle)) {
                return true;
            }
        }
        throw new DoesNotExistException("Course " + courseTitle + " does not exist.");
    }*/
    
    /*public Course sameCourseTitle() throws DoesNotExistException {
        Query query = em.createNamedQuery("Course.findByTitle", Course.class);
        List<Course> courses = query.getResultList();
        if (courses.size() > 0) {
            return courses.get(0);
        }
        throw new DoesNotExistException("Food " + courseTitle + " does not exist.");
    }*/

    public Course findCourseByTitle() throws DoesNotExistException{
        Query query = em.createNamedQuery("Course.findByTitle", Course.class);
        List<Course> courses = query.setParameter("title", courseTitle).getResultList();
        if (courses.size() > 0) {
            return courses.get(0);
        }
        throw new DoesNotExistException("Course " + courseTitle + " does not exist.");
    }
    public Course searchCourse(){
        try {
            return findCourseByTitle();
        } catch (DoesNotExistException ex) {
            System.out.println(ex.getMessage());
        }
        // empty values
        this.courseTitle = "";
        return null;
    
    }
    
    public Boolean doesCourseExist(){
        Query query = em.createNamedQuery("Course.findByTitle", Course.class);
        List<Course> courses = query.setParameter("title", courseTitle).getResultList();
        return courses.size() > 0;
    }
            
    
    /*public Course searchTeacherCourse(){
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
    
    }*/

    public void createACourse() throws AlreadyExistsException {
        if(!doesCourseExist()){
            Course newCourse = new Course();
            newCourse.setTitle(courseTitle);
            newCourse.setPrice(price);
            em.persist(newCourse);
            //need to do something for relation table?
        }
        throw new AlreadyExistsException("Course " + courseTitle + " already exist.");
    }

    public void deleteACourse(){
        User t = LoginBean.getUserLoggedIn();
        if(searchCourse().getFkTeacherId().equals(t.getUserId())){
            Query q = em.createNamedQuery("Course.findByTitle", Course.class);
            List<Course> c = q.setParameter("title", courseTitle).getResultList();
            em.remove(c.get(0));
        }
        courseTitle = "";
    }
    
    /*public String getTeacherName(Course c){
        Integer teacherId = c.getFkTeacherId();
        Query q = em.createNamedQuery("User.findByUserId", User.class).setParameter("userId", teacherId);
        
        return (String) q.getResultList().get(0);
        
    }*/
    
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
