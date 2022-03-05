package bg.softUni.services;

import bg.softUni.models.Account;
import bg.softUni.models.User;
import bg.softUni.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void withdrawnMoney(BigDecimal money, Long id) {

        Account account = this.accountRepository
                .findById(id)
                .orElseThrow();

        if (account.getBalance().compareTo(money) < 0) {
            System.out.println("Lack of balance");
        }

        account.setBalance(account.getBalance().subtract(money));
        this.accountRepository.save(account);
    }

    @Override
    public void transferMoney(BigDecimal money, Long id) {
        User user = accountRepository.findAccountById(id).getUser();

        Account account = this.accountRepository.findById(id).orElseThrow();

        account.setBalance(account.getBalance().add(money));
        this.accountRepository.save(account);
    }
}
