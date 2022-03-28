package softuni.exam.instagraphlite.models.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class UserNameDTO {

    @XmlElement(name = "username")
    private String username;

    public UserNameDTO() {
    }

    public String getUsername() {
        return username;
    }
}
