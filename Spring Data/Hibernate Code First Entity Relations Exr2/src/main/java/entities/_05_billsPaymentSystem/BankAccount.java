package entities._05_billsPaymentSystem;

import javax.persistence.*;

@Entity
@Table(name = "bank_account")
@DiscriminatorValue(value = "bank_account")
public class BankAccount extends BillingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "swift_code")
    private String swiftCode;

    public BankAccount() {
    }

    public BankAccount(String name, String swiftCode) {
        this.name = name;
        this.swiftCode = swiftCode;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }
}
