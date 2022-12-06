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
    
    public static Transaction createTransaction (Student student, Teacher teacher, double amount){
        return new Transaction(student, teacher, amount);
    }


    @Override
    public String toString() {
        return "Transaction between: " + this.student.getUsername() + " and " + this.teacher.getUsername() + " = " + this.amount;
    }

}
