package coursewebsite.models;

import coursewebsite.models.Student;
import coursewebsite.models.Teacher;

public class Transaction {
    private final Student student;
    private final Teacher teacher;
    private final double amount;

    Transaction(Student student, Teacher teacher, double amount) {
        this.student = student;
        this.teacher = teacher;
        this.amount= amount;
            

    }

    public Student getTransactionStudent() { return this.student; }

    public Teacher getTransactionTeacher() { return this.teacher; }

    public double getTransactionAmount() { return this.amount; }

    @Override
    public String toString() {
        return "Transaction between: " + this.student.getUsername() + " and " + this.teacher.getUsername() + " = " + this.amount;
    }

}
