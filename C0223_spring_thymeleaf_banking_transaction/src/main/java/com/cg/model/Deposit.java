package com.cg.model;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "deposits")
public class Deposit extends BasseEntity implements Validator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // referencedColumnName: tham chiếu tới cột id của bảng customer
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    @Column(precision = 10, scale = 0, nullable = false)
    private BigDecimal transactionAmount;

    public Deposit() {
    }

    public Deposit(Long id, Customer customer, BigDecimal transactionAmount) {
        this.id = id;
        this.customer = customer;
        this.transactionAmount = transactionAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Deposit.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Deposit deposit = (Deposit) target;

        BigDecimal transactionAmount = deposit.getTransactionAmount();
        BigDecimal maxTransactionAmount = BigDecimal.valueOf(0L);
        BigDecimal maxBalanceAmount = BigDecimal.valueOf(10000000000L);

        if (transactionAmount == null) {
            errors.rejectValue("transactionAmount", "transactionAmount.null");
        } else {
            BigDecimal updateBalance = deposit.getCustomer().getBalance().add(transactionAmount);

            if (transactionAmount.compareTo(maxTransactionAmount) < 0) {
                errors.rejectValue("transactionAmount", "transactionAmount.min");
            }
            if (transactionAmount.compareTo(maxBalanceAmount) > 0) {
                errors.rejectValue("transactionAmount", "transactionAmount.max");
            }
        }
    }
}
