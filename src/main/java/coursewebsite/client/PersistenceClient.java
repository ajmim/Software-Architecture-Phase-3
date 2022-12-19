package coursewebsite.client;

import coursewebsite.exceptions.AlreadyExistsException;
import coursewebsite.exceptions.DoesNotExistException;
import coursewebsite.models.Course;
import coursewebsite.models.User;
import java.io.BufferedReader;
import java.util.List;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class PersistenceClient {
    
    //check urls
    private static final String COURSE_URL = "http://localhost:8080/soar3/resources/coursewebsite.models.course";
    private static final String USER_URL = "http://localhost:8080/soar3/resources/coursewebsite.models.user";

    private static Client client;
    private static WebTarget target;
    private static PersistenceClient instance;

    private PersistenceClient() {
        PersistenceClient.client = ClientBuilder.newClient();
    }

    public static PersistenceClient getInstance() {
        if (instance == null) {
            instance = new PersistenceClient();
        }
        return instance;
    }
    
    private Document parseDocument(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xml)));
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    
    public void api() throws MalformedURLException, IOException{
        
        // Set the API endpoint URL
      String apiUrl = "https://university-college-list-and-rankings.p.rapidapi.com/api/test";

      // Create a URL object
      URL url = new URL(apiUrl);

      // Open a connection to the API
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      // Set the request method to GET
      conn.setRequestMethod("GET");

      // Set any necessary headers or authentication credentials
      // For example:
      conn.setRequestProperty("X-RapidAPI-Key", "b5690ea5c9msh62544f995c93592p129fd6jsncbe94a4f4f40");
      conn.setRequestProperty("X-RapidAPI-Host", "university-college-list-and-rankings.p.rapidapi.com");

      // Make the request
      conn.connect();

      // Get the response code
      int responseCode = conn.getResponseCode();

      // Read the response data
      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      reader.close();

      // Print the response code and data
      System.out.print("Response code: " + responseCode);
      System.out.print("Response data: " + response.toString());

    }
    
    
    
    //--------------------------------USER--------------------------------------

    public User checkPassword(String username, String password) throws DoesNotExistException {
        User u = getUsersByName(username);
        if (u.getUsername().equals(username) & u.getPassword().equals(password)) {
            return u;
        }
        throw new DoesNotExistException("User " + username + " does not exist.");
    }

    public boolean emailExists(String email) throws AlreadyExistsException {
        return client.target(USER_URL + "/emailExists/" + email).request().get().readEntity(Boolean.class);
    }

    public void createUser(User user) {
        client.target(USER_URL + "/create").request().post(Entity.entity(user, "application/xml"));
    }

    public void updateUser(User user) {
        client.target(USER_URL + "/edit/" + user.getUserId()).request().put(Entity.entity(user, "application/xml"));
    }

    public void removeUser(int id) {
        client.target(USER_URL + "/remove/" + id).request().delete().readEntity(String.class);
    }

    public User getUserById(int id) {
        return parseUser(client.target(USER_URL + "/find/" + id).request().get().readEntity(String.class));
    }

    public User getUsersByName(String username) {
        User u = parseUser(client.target(USER_URL + "/findByName/" + username).request().get(String.class));        
        return u;
    }
    public List<User> getAllUsers() {
        return parseUserList(client.target(USER_URL).request().get(String.class));
    }
    
    public ArrayList<User> getAllTeachers() {
        List<User> tmp = parseUserList(client.target(USER_URL).request().get(String.class));
        ArrayList<User> teachers = new ArrayList();
        for(User u : tmp){
            if(u.getCategory().equals("teacher")){
                teachers.add(u);
            }
        }
        return teachers;
    }
    
    public ArrayList<User> getAllStudents() {
        List<User> tmp = parseUserList(client.target(USER_URL).request().get(String.class));
        ArrayList<User> students = new ArrayList();
        for(User u : tmp){
            if(u.getCategory().equals("student")){
                students.add(u);
            }
        }
        return students;
    }

    private List<User> parseUserList(String xml) {
        List<User> userList = new ArrayList<>();
        NodeList list = parseDocument(xml).getElementsByTagName("user");
        for (int i = 0; i < list.getLength(); i++) {
            Element e = (Element) list.item(i);

            User user = new User();
            user.setBalance(Double.valueOf(e.getElementsByTagName("balance").item(0).getTextContent()));
            user.setEmail(e.getElementsByTagName("email").item(0).getTextContent());
            user.setFirstname(e.getElementsByTagName("firstname").item(0).getTextContent());
            user.setLastname(e.getElementsByTagName("lastname").item(0).getTextContent());
            user.setPassword(e.getElementsByTagName("password").item(0).getTextContent());
            user.setUserId(Integer.valueOf(e.getElementsByTagName("userId").item(0).getTextContent()));
            user.setUsername(e.getElementsByTagName("username").item(0).getTextContent());
            user.setCategory(e.getElementsByTagName("category").item(0).getTextContent());
            
            userList.add(user);
        }
        return userList;
    }

    private User parseUser(String xml) {
        if (xml.length() == 0) {
            return null;
        }
        Element e = (Element) parseDocument(xml).getElementsByTagName("user").item(0);

        User user = new User();
        user.setBalance(Double.valueOf(e.getElementsByTagName("balance").item(0).getTextContent()));
        user.setEmail(e.getElementsByTagName("email").item(0).getTextContent());
        user.setFirstname(e.getElementsByTagName("firstname").item(0).getTextContent());
        user.setLastname(e.getElementsByTagName("lastname").item(0).getTextContent());
        user.setPassword(e.getElementsByTagName("password").item(0).getTextContent());
        user.setUserId(Integer.valueOf(e.getElementsByTagName("userId").item(0).getTextContent()));
        user.setUsername(e.getElementsByTagName("username").item(0).getTextContent());
        user.setCategory(e.getElementsByTagName("category").item(0).getTextContent());
        

        return user;
    }

    //--------------------------------COURSE------------------------------------

    
    public Course getCourseByTitle(String title){
        return parseCourse(client.target(COURSE_URL + "/findByTitle/" + title).request().get().readEntity(String.class));
    }
    
    public List<Course> getAllCourses() {
        return parseCourseList(client.target(COURSE_URL).request().get(String.class));
    }
    
    public void createCourse(Course c) {
        client.target(COURSE_URL + "/create").request().post(Entity.entity(c, "application/xml"));
    }
    
    public void updateCourse(Course course) {
        client.target(COURSE_URL + "/edit/" + course.getCourseId()).request().put(Entity.entity(course, "application/xml"));
    }
    
    public void removeCourse(int id) {
        client.target(COURSE_URL + "/remove/" + id).request().delete().readEntity(String.class);
    }
    
    public List<Course> getStudentCourses(int id){
        return parseCourseList(client.target(USER_URL + "/getStudentCourses/" + id).request().get(String.class));
    }
    
    public List<Course> getTeacherCourses(int id) {
        return parseCourseList(client.target(USER_URL + "/getTeacherCourses/" + id).request().get(String.class));
    }
    
    public List<User> getEnrolledStudents(int id) {
        return parseUserList(client.target(COURSE_URL + "/enrolled/" + id).request().get(String.class));
    }
    
    private List<Course> parseCourseList(String xml) {
        List<Course> courseList = new ArrayList<>();
        NodeList list = parseDocument(xml).getElementsByTagName("course");
        for (int i = 0; i < list.getLength(); i++) {
            Element e = (Element) list.item(i);

            Course c = new Course();
            c.setCourseId(Integer.valueOf(e.getElementsByTagName("courseId").item(0).getTextContent()));
            c.setTitle(e.getElementsByTagName("title").item(0).getTextContent());
            c.setPrice(Double.valueOf(e.getElementsByTagName("price").item(0).getTextContent()));
            
            Element user_xml = (Element) e.getElementsByTagName("fkTeacherId").item(0);

            User user = new User();
            user.setBalance(Double.valueOf(user_xml.getElementsByTagName("balance").item(0).getTextContent()));
            user.setEmail(user_xml.getElementsByTagName("email").item(0).getTextContent());
            user.setFirstname(user_xml.getElementsByTagName("firstname").item(0).getTextContent());
            user.setLastname(user_xml.getElementsByTagName("lastname").item(0).getTextContent());
            user.setPassword(user_xml.getElementsByTagName("password").item(0).getTextContent());
            user.setUserId(Integer.valueOf(user_xml.getElementsByTagName("userId").item(0).getTextContent()));
            user.setUsername(user_xml.getElementsByTagName("username").item(0).getTextContent());
            user.setCategory(user_xml.getElementsByTagName("category").item(0).getTextContent());
            
            c.setFkTeacherId(user);
            
            courseList.add(c);
        }
        return courseList;
    }
    
    private Course parseCourse(String xml) {
        if (xml.length() == 0) {
            return null;
        }
        Element e = (Element) parseDocument(xml).getElementsByTagName("course").item(0);
        
        Course c = new Course();
        c.setCourseId(Integer.valueOf(e.getElementsByTagName("courseId").item(0).getTextContent()));
        c.setTitle(e.getElementsByTagName("title").item(0).getTextContent());
        c.setPrice(Double.valueOf(e.getElementsByTagName("price").item(0).getTextContent()));
        
        Element user_xml = (Element) e.getElementsByTagName("fkTeacherId").item(0);
        User user = new User();
        user.setBalance(Double.valueOf(user_xml.getElementsByTagName("balance").item(0).getTextContent()));
        user.setEmail(user_xml.getElementsByTagName("email").item(0).getTextContent());
        user.setFirstname(user_xml.getElementsByTagName("firstname").item(0).getTextContent());
        user.setLastname(user_xml.getElementsByTagName("lastname").item(0).getTextContent());
        user.setPassword(user_xml.getElementsByTagName("password").item(0).getTextContent());
        user.setUserId(Integer.valueOf(user_xml.getElementsByTagName("userId").item(0).getTextContent()));
        user.setUsername(user_xml.getElementsByTagName("username").item(0).getTextContent());
        user.setCategory(user_xml.getElementsByTagName("category").item(0).getTextContent());
        c.setFkTeacherId(user);
            
        return c;
    }

}
