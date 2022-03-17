package bg.softuni.jsonexr.models.dtos;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class UserSoldDto {

    @Expose
    private String firstName;

    @Expose
    private String lastName;

    @Expose
    private Set<ProductWithBuyerDto> products;

    public UserSoldDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<ProductWithBuyerDto> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductWithBuyerDto> products) {
        this.products = products;
    }
}
