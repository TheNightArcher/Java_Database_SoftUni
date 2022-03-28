package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dtos.UserSeedDTO;
import softuni.exam.instagraphlite.models.entity.User;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class UserServiceImpl implements UserService {

    private static final String USERS_FILE_PATH = "src/main/resources/files/users.json";

    private final UserRepository userRepository;

    private final PictureService pictureService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PictureService pictureService, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.userRepository = userRepository;
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return userRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(USERS_FILE_PATH));
    }

    @Override
    public String importUsers() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readFromFileContent(), UserSeedDTO[].class))
                .filter(userSeedDTO -> {
                    boolean isValid = validationUtil.isValid(userSeedDTO);

                    return isValid(sb, userSeedDTO, isValid);
                })
                .map(userSeedDTO -> {

                    User user = modelMapper.map(userSeedDTO, User.class);

                    user.setProfilePicture(pictureService.getPicture(userSeedDTO.getProfilePicture()));

                    return user;
                })
                .forEach(userRepository::save);

        return sb.toString().trim();
    }

    @Override
    public String exportUsersWithTheirPosts() {
     return null;
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    private boolean isValid(StringBuilder sb, UserSeedDTO userSeedDTO, boolean isValid) {
        if (userSeedDTO.getUsername() == null) {
            sb.append("Invalid User")
                    .append(System.lineSeparator());
            return false;

        } else if (userSeedDTO.getPassword() == null) {
            sb.append("Invalid User")
                    .append(System.lineSeparator());
            return false;

        } else if (userRepository.findByUsername(userSeedDTO.getUsername()) != null) {

            sb.append("Invalid User")
                    .append(System.lineSeparator());
            return false;

        } else if (pictureService.getPicture(userSeedDTO.getProfilePicture()) == null) {
            sb.append("Invalid User")
                    .append(System.lineSeparator());
            return false;

        } else {
            sb.append(isValid ? String.format("Successfully imported User: %s",
                            userSeedDTO.getUsername())

                            : "Invalid User")
                    .append(System.lineSeparator());

            return isValid;
        }
    }
}
