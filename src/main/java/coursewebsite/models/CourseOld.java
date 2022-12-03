package coursewebsite.models;

import coursewebsite.Database.Database;

import java.util.ArrayList;
import java.util.Objects;

public class CourseOld {

    private String title;
    private Teacher teacher;
    private double price;
    private final ArrayList<Student> enrolled_students = new ArrayList<>();
 

    public CourseOld(String title, Teacher teacher, double price) {
        this.title = title;
        this.teacher = teacher;
        this.price = price;
    }

   
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitle(){
        return this.title;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public double getPrice() {
        return this.price;
    }
    
    public void setTeacher(Teacher teacher){
        this.teacher = teacher;
    }
    
    public Teacher getTeacher() {
        return this.teacher;
    }
    
    public ArrayList<Student> getEnrolledStudents() {
        return this.enrolled_students;
    }

    public void addEnrolledStudent(Student student){
        this.enrolled_students.add(student);
    }
    public void deleteStudent(Student student) {
        this.enrolled_students.remove(student);
    }

   
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CourseOld)) {
            return false;
        }
        CourseOld that = (CourseOld) o;
        return (Objects.equals(this.title, that.getTitle())) // same title
                && (this.teacher == that.getTeacher()); // same teacher
                // ajouter d`autres checks ici...
    }
    
    @Override
    public String toString() {
        return "Course " + title + " given by " + teacher.getUsername() + " costs " + price + " CHF";

        /*return "Course "
                + "\nTitle=" + title
                + "\nPrice=" + price
                + "\nRating=" + getRating()
                + "\nTeacher=" + teacher;*/
    }
}
    
    
    
    
    
    
