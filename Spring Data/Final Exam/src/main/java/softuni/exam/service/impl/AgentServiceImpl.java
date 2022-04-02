package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.AgentSeedDTO;
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

    private final static String AGENTS_FILE_PATH = "src/main/resources/files/json/agents.json";

    private final AgentRepository agentRepository;
    private final TownService townService;

    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public AgentServiceImpl(AgentRepository agentRepository, TownService townService, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.agentRepository = agentRepository;
        this.townService = townService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files.readString(Path.of(AGENTS_FILE_PATH));
    }

    @Override
    public String importAgents() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readAgentsFromFile(), AgentSeedDTO[].class))
                .filter(agentSeedDTO -> {
                    boolean isValid = validationUtil.isValid(agentSeedDTO)
                            && !isExistsAgentFirstName(agentSeedDTO.getFirstName());

                    sb.append(isValid ? String.format("Successfully imported agent - %s %s",
                                    agentSeedDTO.getFirstName(),
                                    agentSeedDTO.getLastName())

                                    : "Invalid agent")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(agentSeedDTO -> {
                    Agent agent = modelMapper.map(agentSeedDTO, Agent.class);

                    agent.setTown(townService.findTown(agentSeedDTO.getTown()));

                    return agent;
                })
                .forEach(agentRepository::save);

        return sb.toString().trim();
    }

    @Override
    public Agent findAgent(String name) {
        return agentRepository.findByFirstName(name);
    }

    @Override
    public boolean isAgentExists(String name) {
        return agentRepository.existsByFirstName(name);
    }

    private boolean isExistsAgentFirstName(String firstName) {
        return agentRepository.existsByFirstName(firstName);
    }
}
