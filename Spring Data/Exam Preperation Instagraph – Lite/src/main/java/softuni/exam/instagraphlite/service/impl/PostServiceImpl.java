package softuni.exam.instagraphlite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dtos.PostSeedDTO;
import softuni.exam.instagraphlite.models.dtos.PostSeedRootDTO;
import softuni.exam.instagraphlite.models.entity.Post;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.PostService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;
import softuni.exam.instagraphlite.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PostServiceImpl implements PostService {

    private static final String POSTS_FILE_PATH = "src/main/resources/files/posts.xml";

    private final PostRepository postRepository;
    private final PictureService pictureService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, PictureService pictureService, UserService userService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.postRepository = postRepository;
        this.pictureService = pictureService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return postRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(POSTS_FILE_PATH));
    }

    @Override
    public String importPosts() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(POSTS_FILE_PATH, PostSeedRootDTO.class)
                .getPosts()
                .stream()
                .filter(postSeedDTO -> {
                    boolean isValid = validationUtil.isValid(postSeedDTO);

                    return isValid(sb, postSeedDTO, isValid);
                })
                .map(postSeedDTO -> {
                    Post post = modelMapper.map(postSeedDTO, Post.class);

                    post.setPicture(pictureService.getPicture(postSeedDTO.getPicture().getPath()));
                    post.setUser(userService.getUser(postSeedDTO.getUser().getUsername()));

                    return post;
                })
                .forEach(postRepository::save);

        return sb.toString().trim();
    }

    @Override
    public int getCountOfPostByUser(int id) {
        return postRepository.findByIdCount(id);
    }

    @Override
    public String getCaption(int id) {
        return postRepository.findByIdCaption(id);
    }

    private boolean isValid(StringBuilder sb, PostSeedDTO postSeedDTO, boolean isValid) {
        if (postSeedDTO.getCaption() == null) {
            sb.append("Invalid Post")
                    .append(System.lineSeparator());
            return false;

        } else if (userService.getUser(postSeedDTO.getUser().getUsername()) == null) {
            sb.append("Invalid Post")
                    .append(System.lineSeparator());
            return false;

        } else if (pictureService.getPicture(postSeedDTO.getPicture().getPath()) == null) {
            sb.append("Invalid Post")
                    .append(System.lineSeparator());
            return false;

        } else {
            sb.append(isValid ? String.format("Successfully imported Post, made by %s",
                            postSeedDTO.getUser().getUsername())

                            : "Invalid Post")
                    .append(System.lineSeparator());

            return isValid;
        }
    }
}
