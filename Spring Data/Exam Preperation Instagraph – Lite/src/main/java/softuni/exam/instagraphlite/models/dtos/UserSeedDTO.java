package softuni.exam.instagraphlite.models.dtos;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Size;

public class UserSeedDTO {

    @Expose
    @Size(min = 2, max = 18)
    private String username;

    @Expose
    @Size(min = 4)
    private String password;

    @Expose
    private String profilePicture;

    public UserSeedDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
