package coursewebsite.models;

import coursewebsite.Database.Database;

import java.util.ArrayList;
import java.util.Objects;

public class Course {

    private String title;
    private Teacher teacher;
    private double price;
    private final ArrayList<Student> enrolled_students = new ArrayList<>();
    private final ArrayList<Review> reviewList = new ArrayList<>();

    public Course(String title, Teacher teacher, double price) {
        this.title = title;
        this.teacher = teacher;
        this.price = price;
    }

    public ArrayList<Review> getReviewList() { return this.reviewList; }

    public void addReview(Review review) {
        this.reviewList.add(review);

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

    public double getRating() {
        double mean = 0;
        for (Review review: reviewList) {
            mean += review.getRating();
        }
        return mean / reviewList.size();
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Course)) {
            return false;
        }
        Course that = (Course) o;
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
    
    
    
    
    
    
