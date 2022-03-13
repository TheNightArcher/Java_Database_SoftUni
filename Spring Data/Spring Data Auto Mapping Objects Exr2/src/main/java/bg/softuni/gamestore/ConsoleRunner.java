package bg.softuni.gamestore;

import bg.softuni.gamestore.models.dto.GameAddDto;
import bg.softuni.gamestore.models.dto.UserLoginDto;
import bg.softuni.gamestore.models.dto.UserRegisterDto;
import bg.softuni.gamestore.service.GameService;
import bg.softuni.gamestore.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final BufferedReader bufferedReader;
    private final UserService userService;
    private final GameService gameService;

    public ConsoleRunner(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    }


    @Override
    public void run(String... args) throws Exception {

        while (true) {
            System.out.println("Please enter command");

            String[] commandParts = bufferedReader.readLine().split("\\|");

            switch (commandParts[0]) {
                case "RegisterUser" -> userService.registerUser(
                        new UserRegisterDto(
                                commandParts[1],
                                commandParts[2],
                                commandParts[3],
                                commandParts[4]
                        ));
                case "LoginUser" -> userService.loginUser(new UserLoginDto(
                        commandParts[1],
                        commandParts[2]
                ));
                case "Logout" -> userService
                        .logout();
                case "AddGame" -> gameService
                        .addGame(new GameAddDto(
                                commandParts[1],
                                new BigDecimal(commandParts[2]),
                                Float.parseFloat(commandParts[3]),
                                commandParts[4],
                                commandParts[5],
                                commandParts[6],
                                commandParts[7]
                        ));

                // TODO: Edit and Delete

            }
        }

    }
}
