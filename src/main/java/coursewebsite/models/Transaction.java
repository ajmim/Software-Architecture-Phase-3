package coursewebsite.models;

import coursewebsite.exceptions.InsufficientBalanceException;
import coursewebsite.models.Course;
import coursewebsite.models.Student;
import coursewebsite.models.Teacher;

public class Transaction {
    private final Student student;
    private final Teacher teacher;
    private final double amount;
    private final boolean success;

    Transaction(Student student, Teacher teacher, double amount) {
        this.student = student;
        this.teacher = teacher;
        this.amount= amount;

        if (student.getBalance() >= amount) {
            student.addBalance(-amount);
            teacher.addBalance(amount);
            student.userTransactions.add(this);
            teacher.userTransactions.add(this);
            this.success = true;
        } else {
            this.success = false;
        }
    }

    public boolean isTransactionSuccess() { return this.success; }

    public Student getTransactionStudent() { return this.student; }

    public Teacher getTransactionTeacher() { return this.teacher; }

    public double getTransactionAmount() { return this.amount; }

    @Override
    public String toString() {
        return "Transaction between: " + this.student.getUsername() + " and " + this.teacher.getUsername() + " = " + this.amount;
    }

}
