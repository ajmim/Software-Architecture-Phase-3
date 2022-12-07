package coursewebsite.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "course")
@XmlRootElement
@SuppressWarnings("unchecked")
@NamedQueries({
    @NamedQuery(name = "Course.findAll", query = "SELECT c FROM Course c"),
    @NamedQuery(name = "Course.findByCourseId", query = "SELECT c FROM Course c WHERE c.courseId = :courseId"),
    @NamedQuery(name = "Course.findByTitle", query = "SELECT c FROM Course c WHERE c.title = :title"),
    @NamedQuery(name = "Course.findByPrice", query = "SELECT c FROM Course c WHERE c.price = :price")})
public class Course implements Serializable {
    @PersistenceContext(unitName = "soar_PU")
    private EntityManager em;
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COURSE_ID")
    private Integer courseId;
    @Size(max = 100)
    @Column(name = "TITLE")
    private String title;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRICE")
    private Double price;
    @JoinTable(name = "responsible_for", joinColumns = {
        @JoinColumn(name = "FK_COURSE_TEACHER_ID", referencedColumnName = "COURSE_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "FK_FK_USER_TEACHER_ID", referencedColumnName = "FK_USER_TEACHER_ID")})
    @ManyToMany
    private Collection<Teacher> teacherCollection;
    @JoinTable(name = "enrolled", joinColumns = {
        @JoinColumn(name = "FK_COURSE_STUDENT_ID", referencedColumnName = "COURSE_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "FK_FK_USER_STUDENT_ID", referencedColumnName = "FK_USER_STUDENT_ID")})
    @ManyToMany
    private Collection<Student> studentCollection;

    public Course() {
    }
    
    public Teacher getTeacher(){ //to TEST, remove the joins? ---------------------------------------
        Query q = em.createQuery(
                "SELECT u"
                        + "FROM user u"
                        + "where (select fk_fk_user_teacher_ID FROM responsible_for WHERE course.course_ID = :courseID) = u.user_id"
                        + "INNER JOIN u on u.user_id = student.fk_user_student_id"
                        + "INNER JOIN u on u.user_id = t.fk_user_teacher_id"
                        + "INNER JOIN student s on s.fk_user_student_id = enrolled.fk_fk_user_student_id"
                        + "INNER JOIN teacher t on t.fk_user_teacher_id = responsible_for.fk_fk_user_teacher_id"
                        + "INNER JOIN course c on c.course_id = enrolled.fk_course_student_id"
                        + "INNER JOIN course c on c.course_id = responsible_for.fk_course_teacher_id"
        );
        List<Teacher> t = q.getResultList();
        return t.get(0);
        
    }
    
    public Course(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @XmlTransient
    public Collection<Teacher> getTeacherCollection() {
        return teacherCollection;
    }

    public void setTeacherCollection(Collection<Teacher> teacherCollection) {
        this.teacherCollection = teacherCollection;
    }

    @XmlTransient
    public Collection<Student> getStudentCollection() {
        return studentCollection;
    }

    public void setStudentCollection(Collection<Student> studentCollection) {
        this.studentCollection = studentCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (courseId != null ? courseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Course)) {
            return false;
        }
        Course other = (Course) object;
        if ((this.courseId == null && other.courseId != null) || (this.courseId != null && !this.courseId.equals(other.courseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "coursewebsite.models.Course[ courseId=" + courseId + " ]";
    }
    
}
