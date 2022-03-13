package bg.softuni.gamestore.service.impl;

import bg.softuni.gamestore.models.dto.GameAddDto;
import bg.softuni.gamestore.models.entities.Game;
import bg.softuni.gamestore.repositories.GameRepository;
import bg.softuni.gamestore.service.GameService;
import bg.softuni.gamestore.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public GameServiceImpl(GameRepository gameRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.gameRepository = gameRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void addGame(GameAddDto gameAddDto) {

        Set<ConstraintViolation<GameAddDto>> violations =
                validationUtil.getViolations(gameAddDto);

        if (!violations.isEmpty()){
            violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            return;
        }

        Game game = modelMapper.map(gameAddDto,Game.class);
        game.setReleaseDate(LocalDate.parse(gameAddDto.getReleaseDate(),
                DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        gameRepository.save(game);

        System.out.printf("Added %s\n",game.getTitle());
    }
}
