package com.example.football.service.impl;

import com.example.football.models.dto.TeamSeedDTO;
import com.example.football.models.entity.Team;
import com.example.football.repository.TeamRepository;
import com.example.football.service.TeamService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TeamServiceImpl implements TeamService {

    public static final String TEAMS_FILE_PATH = "src/main/resources/files/json/teams.json";

    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return Files.readString(Path.of(TEAMS_FILE_PATH));
    }

    @Override
    public String importTeams() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readTeamsFileContent(), TeamSeedDTO[].class))
                .filter(teamSeedDTO -> {
                    boolean isValid = validationUtil.isValid(teamSeedDTO);

                    sb.append(isValid ? String.format("Successfully imported Team %s - %d",
                                    teamSeedDTO.getName(),
                                    teamSeedDTO.getFanBase())

                                    : "Invalid Team")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(teamSeedDTO -> modelMapper.map(teamSeedDTO, Team.class))
                .forEach(teamRepository::save);

        return sb.toString();
    }
}
