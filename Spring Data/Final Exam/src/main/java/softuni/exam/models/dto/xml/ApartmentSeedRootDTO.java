package softuni.exam.models.dto.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "apartments")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApartmentSeedRootDTO {

    @XmlElement(name = "apartment")
    private List<ApartmentSeedDTO> apartments;

    public List<ApartmentSeedDTO> getApartments() {
        return apartments;
    }

    public void setApartments(List<ApartmentSeedDTO> apartments) {
        this.apartments = apartments;
    }
}
