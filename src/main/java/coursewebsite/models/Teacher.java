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

@Table(name = "teacher")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Teacher.findAll", query = "SELECT t FROM Teacher t"),
    @NamedQuery(name = "Teacher.findByFkUserTeacherId", query = "SELECT t FROM Teacher t WHERE t.fkUserTeacherId = :fkUserTeacherId")})
public class Teacher extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    //@Id
    @EmbeddedId
    @Basic(optional = false)
    @NotNull
    @Column(name = "FK_USER_TEACHER_ID")
    private Integer fkUserTeacherId;
    @ManyToMany(mappedBy = "teacherList")
    private List<Course> courseList;
    @JoinColumn(name = "FK_USER_TEACHER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private User user;

    public Teacher() {
    }

    public Teacher(Integer fkUserTeacherId) {
        this.fkUserTeacherId = fkUserTeacherId;
    }

    public Integer getFkUserTeacherId() {
        return fkUserTeacherId;
    }

    public void setFkUserTeacherId(Integer fkUserTeacherId) {
        this.fkUserTeacherId = fkUserTeacherId;
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
        hash += (fkUserTeacherId != null ? fkUserTeacherId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Teacher)) {
            return false;
        }
        Teacher other = (Teacher) object;
        if ((this.fkUserTeacherId == null && other.fkUserTeacherId != null) || (this.fkUserTeacherId != null && !this.fkUserTeacherId.equals(other.fkUserTeacherId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "coursewebsite.models.Teacher[ fkUserTeacherId=" + fkUserTeacherId + " ]";
    }
    
}
