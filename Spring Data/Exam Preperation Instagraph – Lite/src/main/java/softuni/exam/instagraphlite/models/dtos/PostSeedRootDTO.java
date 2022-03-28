package softuni.exam.instagraphlite.models.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "posts")
@XmlAccessorType(XmlAccessType.FIELD)
public class PostSeedRootDTO {

    @XmlElement(name = "post")
    private List<PostSeedDTO> posts;

    public PostSeedRootDTO() {
    }

    public List<PostSeedDTO> getPosts() {
        return posts;
    }
}
