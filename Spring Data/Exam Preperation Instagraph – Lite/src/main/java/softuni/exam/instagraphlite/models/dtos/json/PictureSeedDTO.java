package softuni.exam.instagraphlite.models.dtos.json;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PictureSeedDTO {

    @Expose
    @NotNull
    private String path;

    @Expose
    @Min(value = 500)
    @Max(value = 60000)
    private double size;

    public PictureSeedDTO() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
