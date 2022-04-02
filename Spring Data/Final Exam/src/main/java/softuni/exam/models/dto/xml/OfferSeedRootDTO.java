package softuni.exam.models.dto.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "offers")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferSeedRootDTO {

    @XmlElement(name = "offer")
    private List<OfferSeedDTO> offers;

    public List<OfferSeedDTO> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferSeedDTO> offers) {
        this.offers = offers;
    }
}
