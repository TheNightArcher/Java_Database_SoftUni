package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TicketsSeedRootDTO;
import softuni.exam.models.entity.Ticket;
import softuni.exam.repository.TicketRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.PlaneService;
import softuni.exam.service.TicketService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TicketServiceImpl implements TicketService {

    private final static String TICKETS_FILE_PATH = "src/main/resources/files/xml/tickets.xml";

    private final TicketRepository ticketRepository;
    private final TownService townService;
    private final PassengerService passengerService;
    private final PlaneService planeService;

    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, TownService townService, PassengerService passengerService, PlaneService planeService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.ticketRepository = ticketRepository;
        this.townService = townService;
        this.passengerService = passengerService;
        this.planeService = planeService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return Files.readString(Path.of(TICKETS_FILE_PATH));
    }

    @Override
    public String importTickets() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(TICKETS_FILE_PATH, TicketsSeedRootDTO.class)
                .getTickets()
                .stream()
                .filter(ticketSeedDTO -> {
                    boolean isValid = validationUtil.isValid(ticketSeedDTO)
                            && !isSerialNumberExist(ticketSeedDTO.getSerialNumber());

                    sb.append(isValid ? String.format("Successfully imported Ticket %s - %s",
                                    ticketSeedDTO.getFromTown().getName(),
                                    ticketSeedDTO.getToTown().getName())

                                    : "Invalid Ticket")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(ticketSeedDTO -> {
                    Ticket ticket = modelMapper.map(ticketSeedDTO, Ticket.class);

                    ticket.setFromTown(townService.getTown(ticketSeedDTO.getFromTown().getName()));
                    ticket.setToTown(townService.getTown(ticketSeedDTO.getToTown().getName()));
                    ticket.setPassenger(passengerService.getEmail(ticketSeedDTO.getPassenger().getEmail()));
                    ticket.setPlane(planeService.getPlane(ticketSeedDTO.getPlane().getRegisterNumber()));

                    return ticket;
                })
                .forEach(ticketRepository::save);

        return sb.toString().trim();
    }

    private boolean isSerialNumberExist(String serialNumber) {
        return ticketRepository.existsBySerialNumber(serialNumber);
    }
}
