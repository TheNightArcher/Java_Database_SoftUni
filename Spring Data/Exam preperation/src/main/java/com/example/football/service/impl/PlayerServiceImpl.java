package com.example.football.service.impl;

import com.example.football.models.dto.PlayerSeedRootDTO;
import com.example.football.models.entity.Player;
import com.example.football.repository.PlayerRepository;
import com.example.football.service.PlayerService;
import com.example.football.service.StatService;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final String PLAYERS_FILE_PATH = "src/main/resources/files/xml/players.xml";

    private final PlayerRepository playerRepository;
    private final TownService townService;
    private final TeamService teamService;
    private final StatService statService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, TownService townService, TeamService teamService, StatService statService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.playerRepository = playerRepository;
        this.townService = townService;
        this.teamService = teamService;
        this.statService = statService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }


    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(Path.of(PLAYERS_FILE_PATH));
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        xmlParser
                .fromFile(PLAYERS_FILE_PATH, PlayerSeedRootDTO.class)
                .getPlayers()
                .stream()
                .filter(playerSeedDTO -> {
                    boolean isValid = validationUtil.isValid(playerSeedDTO);

                    sb.append(isValid ? String.format("Successfully imported Player %s %s - %s",
                                    playerSeedDTO.getFirstName(),
                                    playerSeedDTO.getLastName(),
                                    playerSeedDTO.getPosition().toString())

                                    : "Invalid Player")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(playerSeedDTO -> {
                    Player player = modelMapper.map(playerSeedDTO, Player.class);


                    player.setTown(townService.getTown(playerSeedDTO.getTown().getName()));
                    player.setTeam(teamService.getTeam(playerSeedDTO.getTeam().getName()));
                    player.setStat(statService.getStat(playerSeedDTO.getStat().getId()));

                    return player;
                })
                .forEach(playerRepository::save);

        return sb.toString();
    }

    @Override
    public String exportBestPlayers() {
        StringBuilder sb = new StringBuilder();

        playerRepository.findBestPlayers()
                .forEach(p -> sb.append(String.format("""
                                        Player - %s %s
                                        \tPosition - %s
                                        \tTeam - %s
                                        \tStadium - %s
                                        """,
                                p.getFirstName(),
                                p.getLastName(),
                                p.getPosition(),
                                p.getTeam().getName(),
                                p.getTeam().getStadiumName()))
                        .append(System.lineSeparator()));

        return sb.toString();
    }
}
