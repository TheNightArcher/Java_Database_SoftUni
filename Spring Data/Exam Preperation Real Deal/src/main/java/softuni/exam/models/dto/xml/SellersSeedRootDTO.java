package softuni.exam.models.dto.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sellers")
@XmlAccessorType(XmlAccessType.FIELD)
public class SellersSeedRootDTO {

    @XmlElement(name = "seller")
    private List<SellerSeedDTO> sellers;

    public List<SellerSeedDTO> getSellers() {
        return sellers;
    }
}
