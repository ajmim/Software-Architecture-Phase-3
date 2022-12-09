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
import javax.transaction.Transactional;


@Named(value = "courseBean")
@SessionScoped
public class CourseBean implements Serializable {
   
    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;
    
    private String courseTitle = "";
    private double price = 0.0;
 
 
    public ArrayList<Course> getCourses() {
        return new ArrayList(em.createNamedQuery("Course.findAll", Course.class).getResultList());
    }

    public Course findCourseByTitle() throws DoesNotExistException {
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
    
    public Boolean doesCourseExist() throws AlreadyExistsException {
        Query query = em.createNamedQuery("Course.findByTitle", Course.class);
        List<Course> courses = query.setParameter("title", courseTitle).getResultList();
        if(courses.size() > 0){throw new AlreadyExistsException("Course " + courses.get(0) + " already exists.");}
        return false;
    }
    
    @Transactional
    public void createACourse() throws AlreadyExistsException { 
        try{
            if(!doesCourseExist()){
                Course newCourse = new Course();
                newCourse.setTitle(courseTitle);
                newCourse.setPrice(price);
                newCourse.setFkTeacherId(LoginBean.getUserLoggedIn());
                em.persist(newCourse);
            }
        }catch(AlreadyExistsException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    @Transactional
    public void deleteACourse() {
        User t = LoginBean.getUserLoggedIn();
        Course c = searchCourse();
        if(c.getFkTeacherId().equals(t)){em.remove(c);}
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
