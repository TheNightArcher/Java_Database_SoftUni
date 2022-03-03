package entities._05_billsPaymentSystem;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "credit_card")
@DiscriminatorValue(value = "credit_card")
public class CreditCard extends BillingDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "expiration_month")
    private LocalDate expirationMonth;

    @Column(name = "expiration_year")
    private LocalDate expirationYear;


    public CreditCard() {
    }

    public CreditCard(String cardType, LocalDate expirationMonth, LocalDate expirationYear) {
        this.cardType = cardType;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
    }

    public Long getId() {
        return id;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public LocalDate getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(LocalDate expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public LocalDate getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(LocalDate expirationYear) {
        this.expirationYear = expirationYear;
    }
}
