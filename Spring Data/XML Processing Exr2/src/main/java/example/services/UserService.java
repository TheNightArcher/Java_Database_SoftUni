package example.services;

import example.models.User;
import example.models.dtos.UserSeedDto;

import java.util.List;

public interface UserService {

    long getCount();

    void seedUsers(List<UserSeedDto> users);

     User getRandomUser();
}
