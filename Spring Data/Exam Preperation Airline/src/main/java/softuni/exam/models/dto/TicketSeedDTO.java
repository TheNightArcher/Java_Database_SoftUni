package softuni.exam.models.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
public class TicketSeedDTO {

    @XmlElement(name = "serial-number")
    @Size(min = 2)
    @NotNull
    private String serialNumber;

    @XmlElement
    @Positive
    @NotNull
    private BigDecimal price;

    @XmlElement(name = "take-off")
    @NotNull
    private String takeoff;

    @XmlElement(name = "from-town")
    @NotNull
    private TownNameDTO fromTown;

    @XmlElement(name = "to-town")
    @NotNull
    private TownNameDTO toTown;

    @XmlElement
    @NotNull
    private PassengerEmailDTO passenger;

    @XmlElement()
    @NotNull
    private PlaneRegisterNumber plane;

    public PlaneRegisterNumber getPlane() {
        return plane;
    }

    public void setPlane(PlaneRegisterNumber plane) {
        this.plane = plane;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTakeoff() {
        return takeoff;
    }

    public void setTakeoff(String takeoff) {
        this.takeoff = takeoff;
    }

    public TownNameDTO getFromTown() {
        return fromTown;
    }

    public void setFromTown(TownNameDTO fromTown) {
        this.fromTown = fromTown;
    }

    public TownNameDTO getToTown() {
        return toTown;
    }

    public void setToTown(TownNameDTO toTown) {
        this.toTown = toTown;
    }

    public PassengerEmailDTO getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerEmailDTO passenger) {
        this.passenger = passenger;
    }
}
