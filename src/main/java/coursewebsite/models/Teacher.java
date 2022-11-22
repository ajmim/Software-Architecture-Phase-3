package coursewebsite.models;

import coursewebsite.Database.Database;

public class Teacher extends User {
    public Teacher (String username, String firstName, String lastName, 
            String email, String password){
        super(username,firstName,lastName, email, password);
        //Database.getInstance().getTeachers().add(this);
    }

    @Override
    public String toString() {
        return "Username: " + this.getUsername()
                + "\nFirst name: " + this.getFirstName()
                + "\nLast name: " + this.getLastName()
                + "\nEmail: " + this.getEmail()
                + "\nBalance: " + this.balance;
    }

    /*public void createCourse(String title, double price) {
        Course createdCourse = new Course(title, this, price);
        Database.getInstance().addCourseInApp(createdCourse);
    }*/

}