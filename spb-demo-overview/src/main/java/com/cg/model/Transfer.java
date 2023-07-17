package com.cg.model;


import com.cg.model.dto.transfer.TransferDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;



@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transfers")
public class Transfer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
    private Customer sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id", nullable = false)
    private Customer recipient;

    @Column(name = "transfer_amount", precision = 12, scale = 0, nullable = false)
    private BigDecimal transferAmount;

    @Column (nullable = false)
    private Long fees;

    @Column(name = "fees_amount", precision = 12, scale = 0, nullable = false)
    private BigDecimal feesAmount;

    @Column(name = "transaction_amount", precision = 12, scale = 0, nullable = false)
    private BigDecimal transactionAmount;

    public TransferDTO toTransferDTO() {
        return new TransferDTO()
                .setId(id)
                .setSenderDTO(sender.toCustomerDTO())
                .setRecipientDTO(recipient.toCustomerDTO())
                .setFees(fees)
                .setFeesAmount(feesAmount)
                .setTransferAmount(transferAmount)
                .setTransactionAmount(transactionAmount)
                ;
    }
}
