package bg.softUni;

import bg.softUni.models.Account;
import bg.softUni.models.User;
import bg.softUni.services.AccountService;
import bg.softUni.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;

@Component
public class ConsoleRunner implements CommandLineRunner {
    private UserService userService;
    private AccountService accountService;

    @Autowired
    public ConsoleRunner(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @Override
    public void run(String... args) throws Exception {
        User user = new User("Galin",21);


        Account account = new Account(new BigDecimal("30000"));
        account.setUser(user);

        user.setAccounts(new HashSet<>(){{
            add(account);
        }});

        userService.registerUser(user);

        accountService.withdrawnMoney(new BigDecimal("20000"),account.getId());
        accountService.transferMoney(new BigDecimal("30000"),account.getId());
    }
}
