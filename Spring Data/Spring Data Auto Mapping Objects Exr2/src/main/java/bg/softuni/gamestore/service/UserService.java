package bg.softuni.gamestore.service;

import bg.softuni.gamestore.models.dto.UserLoginDto;
import bg.softuni.gamestore.models.dto.UserRegisterDto;

public interface UserService {
    void registerUser(UserRegisterDto registerUserDto);

    void loginUser(UserLoginDto userLoginDto);

    void logout();
}
