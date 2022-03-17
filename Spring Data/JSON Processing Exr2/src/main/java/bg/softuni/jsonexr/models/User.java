package bg.softuni.jsonexr.models;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int age;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @OneToMany
    private Set<User> friends;

    @OneToMany(mappedBy = "seller",fetch = FetchType.EAGER)
    private Set<Product> products;

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public User() {
        this.friends = new HashSet<>();
    }

    public User(int age, String firstName, String lastName, Set<User> friends) {
        this();
        this.age = age;
        this.firstName = firstName;
        this.lastName = lastName;
        this.friends = friends;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<User> getFriends() {
        return Collections.unmodifiableSet(friends);
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }
}
