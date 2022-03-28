package softuni.exam.instagraphlite.models.dtos;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class PostSeedDTO {

    @XmlElement(name = "caption")
    @Size(min = 21)
    @Column(nullable = false)
    private String caption;

    @XmlElement(name = "user")
    private UserNameDTO user;

    @XmlElement(name = "picture")
    private PicturePath picture;

    public PostSeedDTO() {
    }

    public String getCaption() {
        return caption;
    }

    public UserNameDTO getUser() {
        return user;
    }

    public PicturePath getPicture() {
        return picture;
    }
}
