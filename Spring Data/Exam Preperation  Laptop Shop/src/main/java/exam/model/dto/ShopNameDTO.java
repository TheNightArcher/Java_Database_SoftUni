package exam.model.dto;

import com.google.gson.annotations.Expose;

public class ShopNameDTO {

    @Expose
    private String name;

    public ShopNameDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
