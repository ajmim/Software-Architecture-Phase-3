package coursewebsite.models;

import coursewebsite.Database.Database;
import coursewebsite.exceptions.InsufficientBalanceException;

import javax.xml.crypto.Data;

public class Student extends User {
    public Student (String username, String firstName,
                    String lastName, String email, String password){
        super(username,firstName,lastName, email, password);
        //Database.getInstance().getStudents().add(this);
    }

    public void enroll(Course course) throws InsufficientBalanceException {
        Transaction t = new Transaction(this, course.getTeacher(), course.getPrice());
        if (t.isTransactionSuccess()) {
            this.addUserCourse(course);
            course.addEnrolledStudent(this);
        } else {
            throw new InsufficientBalanceException("Insufficient balance");
        }
    }

    public void increaseBalance(double amount){
        this.setBalance(this.getBalance()+amount);
    }

}