/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursewebsite.models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author moham
 */
@Entity

@Table(name = "student")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s"),
    @NamedQuery(name = "Student.findByFkUserStudentId", query = "SELECT s FROM Student s WHERE s.fkUserStudentId = :fkUserStudentId")})
public class Student extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    //@Id
    @EmbeddedId
    @Basic(optional = false)
    @NotNull
    @Column(name = "FK_USER_STUDENT_ID")
    private Integer fkUserStudentId;
    @ManyToMany(mappedBy = "studentList")
    private List<Course> courseList;
    @JoinColumn(name = "FK_USER_STUDENT_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private User user;

    public Student() {
    }

    public Student(Integer fkUserStudentId) {
        this.fkUserStudentId = fkUserStudentId;
    }

    public Integer getFkUserStudentId() {
        return fkUserStudentId;
    }

    public void setFkUserStudentId(Integer fkUserStudentId) {
        this.fkUserStudentId = fkUserStudentId;
    }

    @XmlTransient
    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
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
        hash += (fkUserStudentId != null ? fkUserStudentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        if ((this.fkUserStudentId == null && other.fkUserStudentId != null) || (this.fkUserStudentId != null && !this.fkUserStudentId.equals(other.fkUserStudentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "coursewebsite.models.Student[ fkUserStudentId=" + fkUserStudentId + " ]";
    }
    
}
