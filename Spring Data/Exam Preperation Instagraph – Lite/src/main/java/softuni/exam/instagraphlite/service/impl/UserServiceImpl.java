package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dtos.json.UserSeedDTO;
import softuni.exam.instagraphlite.models.entities.User;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

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
        return Files
                .readString(Path.of(USERS_FILE_PATH));
    }

    @Override
    public String importUsers() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readFromFileContent(), UserSeedDTO[].class))
                .filter(userSeedDTO -> {
                    boolean isValid = validationUtil.isValid(userSeedDTO)
                            && !isUsernameExist(userSeedDTO.getUsername())
                            && pictureService.isExistPath(userSeedDTO.getProfilePicture());

                    sb.append(isValid ? String.format("Successfully imported User: %s",
                                    userSeedDTO.getUsername())

                                    : "Invalid User")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(userSeedDTO -> {
                    User user = modelMapper.map(userSeedDTO, User.class);

                    user.setProfilePicture(pictureService.getPath(userSeedDTO.getProfilePicture()));

                    return user;
                })
                .forEach(userRepository::save);

        return sb.toString().trim();
    }

    @Override
    public String exportUsersWithTheirPosts() {
        StringBuilder sb = new StringBuilder();

        userRepository.exportUserWithPosts()
                .forEach(u -> {

                    sb.append(String.format("User: %s%n" +
                                    "Post count: %d%n",
                            u.getUsername(),
                            u.getPosts().size()));

                    u.getPosts()
                            .stream()
                            .sorted(Comparator.comparingDouble(p -> p.getPicture().getSize()))
                            .forEach(p -> sb.append(String.format("==Post Details:%n" +
                                            "----Caption: %s%n" +
                                            "----Picture Size: %.2f%n", p.getCaption()
                                    , p.getPicture().getSize())));
                });


        return sb.toString().trim();
    }

    private boolean isUsernameExist(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean isExistUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
