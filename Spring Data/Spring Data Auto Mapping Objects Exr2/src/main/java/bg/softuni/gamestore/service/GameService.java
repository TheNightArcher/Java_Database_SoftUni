package bg.softuni.gamestore.service;

import bg.softuni.gamestore.models.dto.GameAddDto;

public interface GameService {
    void addGame(GameAddDto gameAddDto);
}
