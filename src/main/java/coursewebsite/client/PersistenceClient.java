package coursewebsite.client;

//import ch.unil.doplab.grocerystorerestfulclient.exceptions.AlreadyExistsException;
//import ch.unil.doplab.grocerystorerestfulclient.exceptions.DoesNotExistException;
//import ch.unil.doplab.grocerystorerestfulclient.models.Foods;
//import ch.unil.doplab.grocerystorerestfulclient.models.Users;
import coursewebsite.exceptions.AlreadyExistsException;
import coursewebsite.exceptions.DoesNotExistException;
import coursewebsite.models.Course;
import coursewebsite.models.User;
import java.util.List;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class PersistenceClient {

    private static final String COURSE_URL = "http://localhost:8080/mavenproject2Service/resources/coursewebsite.models.course";
    private static final String USER_URL = "http://localhost:8080/mavenproject2Service/resources/coursewebsite.models.user";

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

    public void completeShopping(int id) {
        client.target(USER_URL + "/completeShopping/" + id).request().get();
    }

    public void removeFromShoppingCart(int uId, int fId) {
        client.target(USER_URL + "/removeFromShoppingCart/" + uId + "/" + fId).request().get();
    }

    public void addToShoppingCart(int uId, int fId) {
        client.target(USER_URL + "/addToShoppingCart/" + uId + "/" + fId).request().get();
    }

    public List<Foods> getAllFoodsInShoppingCart(int id) {
        return parseFoodList(client.target(USER_URL + "/getShoppingCart/" + id).request().get(String.class));
    }

    public User checkPassword(String username, int password) throws DoesNotExistException {
        User u = getUsersByName(username);
        if (u.getUsername().equals(username) & u.getPassword() == password) {
            return u;
        }
        throw new DoesNotExistException("Users " + username + " does not exist.");
    }

    public boolean emailExists(String email) throws AlreadyExistsException {
        return client.target(USER_URL + "/emailExists/" + email).request().get().readEntity(Boolean.class);
    }

    public void createUser(User user) {
        client.target(USER_URL + "/create").request().post(Entity.entity(user, "application/xml"));
    }

    public void updateUser(Users user) {
        client.target(USER_URL + "/edit/" + user.getUserId()).request().put(Entity.entity(user, "application/xml"));
    }

    public void removeUser(int id) {
        client.target(USER_URL + "/remove/" + id).request().get().readEntity(String.class);
    }

    public Users getUserById(int id) {
        return parseUser(client.target(USER_URL + "/find/" + id).request().get().readEntity(String.class));
    }

    public User getUsersByName(String username) {
        User u = parseUser(client.target(USER_URL + "/findByName/" + username).request().get(String.class));
        return u;
    }
    
    public Course getCourseByTitle(String title){
        Course c = parseCourse();
    }

    public List<Users> getAllUsers() {
        return parseUserList(client.target(USER_URL).request().get(String.class));
    }

    private List<Users> parseUserList(String xml) {
        List<Users> userList = new ArrayList<>();
        NodeList list = parseDocument(xml).getElementsByTagName("users");
        for (int i = 0; i < list.getLength(); i++) {
            Element e = (Element) list.item(i);

            Users user = new Users();
            user.setBalance(Double.valueOf(e.getElementsByTagName("balance").item(0).getTextContent()));
            user.setEmail(e.getElementsByTagName("email").item(0).getTextContent());
            user.setFirstName(e.getElementsByTagName("firstName").item(0).getTextContent());
            user.setLastName(e.getElementsByTagName("lastName").item(0).getTextContent());
            user.setPassword(Integer.valueOf(e.getElementsByTagName("password").item(0).getTextContent()));
            user.setUserId(Integer.valueOf(e.getElementsByTagName("userId").item(0).getTextContent()));
            user.setUsername(e.getElementsByTagName("username").item(0).getTextContent());

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
    
    private Course parseCourse(String xml) {
        if (xml.length() == 0) {
            return null;
        }
        Element e = (Element) parseDocument(xml).getElementsByTagName("course").item(0);
        
        Course c = new Course();
        c.setTitle(e.getElementsByTagName("title").item(0).getTextContent());
        c.setPrice(Double.valueOf(e.getElementsByTagName("price").item(0).getTextContent()));
        c.setFkTeacherId(.getElementsByTagName("fkTeacherId").item(0).getTextContent());
        

        return c;
    }

    public void createFood(Foods food) {
        client.target(COURSE_URL + "/create").request().post(Entity.entity(food, "application/xml"));
    }

    public void updateFood(Foods food) {
        client.target(COURSE_URL + "/edit/" + food.getFoodId()).request().put(Entity.entity(food, "application/xml"));
    }

    public void removeFood(int id) {
        client.target(COURSE_URL + "/remove/" + id).request().get().readEntity(String.class);
    }

    public Foods getFoodById(int id) {
        return parseFood(client.target(COURSE_URL + "/find/" + id).request().get().readEntity(String.class));
    }

    public Foods getFoodByName(String foodName) throws DoesNotExistException {
        Foods f = parseFood(client.target(COURSE_URL + "/findByName/" + foodName).request().get(String.class));
        if (f != null) {
            return f;
        }
        throw new DoesNotExistException("Food " + foodName + " does not exist.");
    }

    public List<Foods> getAllFoods() {
        return parseFoodList(client.target(COURSE_URL).request().get(String.class));
    }

    private List<Foods> parseFoodList(String xml) {
        List<Foods> foodList = new ArrayList<>();
        NodeList list = parseDocument(xml).getElementsByTagName("foods");
        for (int i = 0; i < list.getLength(); i++) {
            Element e = (Element) list.item(i);

            Foods food = new Foods();
            food.setFoodId(Integer.valueOf(e.getElementsByTagName("foodId").item(0).getTextContent()));
            food.setFoodName(e.getElementsByTagName("foodName").item(0).getTextContent());
            food.setFoodPrice(Double.valueOf(e.getElementsByTagName("foodPrice").item(0).getTextContent()));
            food.setIngredients(e.getElementsByTagName("ingredients").item(0).getTextContent());

            foodList.add(food);
        }
        return foodList;
    }

    private Foods parseFood(String xml) {
        if (xml.length() == 0) {
            return null;
        }
        Element e = (Element) parseDocument(xml).getElementsByTagName("foods").item(0);

        Foods food = new Foods();
        food.setFoodId(Integer.valueOf(e.getElementsByTagName("foodId").item(0).getTextContent()));
        food.setFoodName(e.getElementsByTagName("foodName").item(0).getTextContent());
        food.setFoodPrice(Double.valueOf(e.getElementsByTagName("foodPrice").item(0).getTextContent()));
        food.setIngredients(e.getElementsByTagName("ingredients").item(0).getTextContent());

        return food;
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

}
