package exam.model.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Size;

public class TownNameJsonDTO {

    @Expose
    @Size(min = 2)
    private String name;

    public TownNameJsonDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
