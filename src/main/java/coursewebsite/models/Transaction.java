package coursewebsite.models;

public class Transaction {
    private final User student;
    private final User teacher;
    private final double amount;

    Transaction(User student, User teacher, double amount) {
        this.student = student;
        this.teacher = teacher;
        this.amount= amount;
    }
    
    public static Transaction createTransaction (User student, User teacher, double amount){
        return new Transaction(student, teacher, amount);
    }


    @Override
    public String toString() {
        return "Transaction between: " + this.student.getUsername() + " and " + this.teacher.getUsername() + " = " + this.amount;
    }

}
