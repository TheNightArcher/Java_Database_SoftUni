package bg.softUni.services;

import bg.softUni.models.Account;
import bg.softUni.models.User;
import bg.softUni.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void withdrawnMoney(BigDecimal money, Long id) {


    }

    @Override
    public void transferMoney(BigDecimal money, Long id) {
        User user = accountRepository.findAccountById(id).getUser();

        if (user != null) {
            if (money.compareTo(BigDecimal.ZERO) >= 0) {
                BigDecimal currentBalance = accountRepository.findAccountById(id)
                        .getBalance()
                        .add(money);
                accountRepository.findAccountById(id).setBalance(currentBalance);
            }
        } else {
            System.out.println("Account id dose not exist!");
        }
    }
}
