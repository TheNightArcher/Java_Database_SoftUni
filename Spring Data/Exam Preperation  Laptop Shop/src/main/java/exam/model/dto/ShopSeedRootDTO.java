package exam.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "shops")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShopSeedRootDTO {

    @XmlElement(name = "shop")
    private List<ShopSeedDTO> shops;

    public ShopSeedRootDTO() {
    }

    public List<ShopSeedDTO> getShops() {
        return shops;
    }

    public void setShops(List<ShopSeedDTO> shops) {
        this.shops = shops;
    }
}
