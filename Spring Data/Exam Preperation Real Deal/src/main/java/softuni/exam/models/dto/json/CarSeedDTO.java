package softuni.exam.models.dto.json;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class CarSeedDTO {

    @Expose
    @Size(min = 2,max = 19)
    @NotNull
    private String make;

    @Expose
    @Size(min = 2,max = 19)
    @NotNull
    private String model;

    @Expose
    @Positive
    @NotNull
    private int kilometers;

    @Expose
    @NotNull
    private String registeredOn;

    public CarSeedDTO() {
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public String getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }
}
