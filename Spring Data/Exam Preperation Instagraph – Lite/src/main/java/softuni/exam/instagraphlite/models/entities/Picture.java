package softuni.exam.instagraphlite.models.entities;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "pictures")
public class Picture extends BaseEntity{

    @Column(unique = true)
    private String path;

    @Column(nullable = false)
    private double size;

    @OneToMany(mappedBy = "profilePicture")
    private Set<User> users;

    public Picture() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
