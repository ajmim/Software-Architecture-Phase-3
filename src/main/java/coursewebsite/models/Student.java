/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursewebsite.models;

import coursewebsite.exceptions.InsufficientBalanceException;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author moham
 */
@Entity
@Table(name = "student")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s"),
    @NamedQuery(name = "Student.findByFkUserTeId", query = "SELECT s FROM Student s WHERE s.fkUserTeId = :fkUserTeId")})
public class Student extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "FK_USER_TE_ID")
    @EmbeddedId //CHECK IF WORK ------------------------------------------------------
    private Integer fkUserTeId;
    @JoinColumn(name = "FK_USER_TE_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private User user;

    public Student() {
    }

    public Student(Integer fkUserTeId) {
        this.fkUserTeId = fkUserTeId;
    }

    public Integer getFkUserTeId() {
        return fkUserTeId;
    }

    public void setFkUserTeId(Integer fkUserTeId) {
        this.fkUserTeId = fkUserTeId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fkUserTeId != null ? fkUserTeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        if ((this.fkUserTeId == null && other.fkUserTeId != null) || (this.fkUserTeId != null && !this.fkUserTeId.equals(other.fkUserTeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "coursewebsite.models.Student[ fkUserTeId=" + fkUserTeId + " ]";
    }
    
    //------------ADDED------------------------
    public void increaseBalance(double amount){
        this.setBalance(this.getBalance()+amount);
    }
    
    public void enroll(Course course) throws InsufficientBalanceException {
        //Transaction t = new Transaction(this, course.getTeacher(), course.getPrice());
        if (this.getBalance() >= course.getPrice()) {
            //this.addUserCourse(course);
            //course.addEnrolledStudent(this);
            this.setBalance(this.getBalance() - course.getPrice());
            course.getTeacher().setBalance(course.getTeacher().getBalance() + course.getPrice());
        } else {
            throw new InsufficientBalanceException("Insufficient balance");
        }
    }
    
}
