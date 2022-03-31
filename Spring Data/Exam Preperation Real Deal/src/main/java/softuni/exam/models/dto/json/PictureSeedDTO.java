package softuni.exam.models.dto.json;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PictureSeedDTO {

    @Expose
    @Size(min = 2,max = 19)
    @NotNull
    private String name;

    @Expose
    @NotNull
    private String dateAndTime;

    @Expose
    @NotNull
    private long car;

    public PictureSeedDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public long getCar() {
        return car;
    }

    public void setCar(long car) {
        this.car = car;
    }
}
