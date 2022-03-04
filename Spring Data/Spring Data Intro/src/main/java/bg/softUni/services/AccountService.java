package bg.softUni.services;

import java.math.BigDecimal;

public interface AccountService {

    void withdrawnMoney(BigDecimal money, Long id);
    void transferMoney(BigDecimal money, Long id);
}
