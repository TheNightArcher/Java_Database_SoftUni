package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AgentSeedDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.repository.AgentRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final TownService townService;
    private final ValidationUtil validationUtil;
    private static final String FILE_PATH = "src/main/resources/files/json/agents.json";

    public AgentServiceImpl(AgentRepository agentRepository, ModelMapper modelMapper, Gson gson, TownService townService, ValidationUtil validationUtil) {
        this.agentRepository = agentRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.townService = townService;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importAgents() throws IOException {
        StringBuilder result = new StringBuilder();

        Arrays.stream(this.gson.fromJson(readAgentsFromFile(), AgentSeedDto[].class))
                .filter(agentSeedDto -> validateData(agentSeedDto, result))
                .map(this::mapToEntity)
                .forEach(this.agentRepository::save);

        return result.toString().trim();
    }

    @Override
    public Agent findByFirstName(String name) {
        return this.agentRepository.findByFirstName(name).orElse(null);
    }

    private Agent mapToEntity(AgentSeedDto agentSeedDto) {
        Agent agent = this.modelMapper.map(agentSeedDto, Agent.class);
        agent.setTown(this.townService.findByTownName(agentSeedDto.getTown()));
        return agent;
    }

    private boolean validateData(AgentSeedDto agentSeedDto, StringBuilder result) {
        if (!this.validationUtil.isValid(agentSeedDto)) {
            result.append("Invalid agent").append(System.lineSeparator());
            return false;
        }

        Agent byFirstName = this.agentRepository.findByFirstName(agentSeedDto.getFirstName()).orElse(null);
        if (byFirstName != null) {
            result.append("Invalid agent").append(System.lineSeparator());
            return false;
        }

        result.append(String.format("Successfully imported agent - %s %s", agentSeedDto.getFirstName(), agentSeedDto.getLastName()))
                .append(System.lineSeparator());
        return true;
    }
}
