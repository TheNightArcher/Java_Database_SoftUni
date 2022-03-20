package bg.softuni.jsonexr.services;

import bg.softuni.jsonexr.models.User;
import bg.softuni.jsonexr.models.dtos.UserSoldDto;
import bg.softuni.jsonexr.models.dtos.UsersWithMoreThenOneSoledProductDto;

import java.io.IOException;
import java.util.List;

public interface UserService {
    void seedUsers() throws IOException;

    User findRandomUser();

    List<UserSoldDto> findAllUserWithMoreThenOneProducts();

    List<UsersWithMoreThenOneSoledProductDto> findAllUsersWithMoreThenOneSoledProduct();
}
