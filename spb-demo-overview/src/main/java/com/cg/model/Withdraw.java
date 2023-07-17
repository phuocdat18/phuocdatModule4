package com.cg.model;

import com.cg.model.dto.withdraw.WithdrawDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;



@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "withdraws")
@Accessors(chain = true)
public class Withdraw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    @Column(name = "transaction_amount", precision = 12, scale = 0, nullable = false)
    private BigDecimal transactionAmount;

    public WithdrawDTO toWithdrawDTO() {
        return new WithdrawDTO()
                .setId(id)
                .setCustomerResDTO(customer.toCustomerResDTO())
                .setTransactionAmount(transactionAmount)
                ;
    }
}
