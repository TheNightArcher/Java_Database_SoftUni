package softuni.exam.instagraphlite.models.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class PicturePath {

    @XmlElement(name = "path")
    private String Path;

    public PicturePath() {
    }

    public String getPath() {
        return Path;
    }
}
