package com.cg.model;

import javax.persistence.*;
import java.util.Date;
import java.math.BigDecimal;

@Entity
@Table(name = "deposits")
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date created_at;
    private Long created_by;
    @Column(columnDefinition = "boolean default false")
    private boolean deleted;
    private Date updated_at;
    private Long updated_by;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    @Column(nullable = false)
    private BigDecimal transaction_amount;

    public Deposit() {
    }

    public Deposit(Date created_at, long created_by, boolean deleted, Date updated_at, long updated_by, BigDecimal transaction_amount) {
        this.created_at = created_at;
        this.created_by = created_by;
        this.deleted = deleted;
        this.updated_at = updated_at;
        this.updated_by = updated_by;
        this.transaction_amount = transaction_amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Long getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Long created_by) {
        this.created_by = created_by;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Long getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(Long updated_by) {
        this.updated_by = updated_by;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(BigDecimal transaction_amount) {
        this.transaction_amount = transaction_amount;
    }
}





