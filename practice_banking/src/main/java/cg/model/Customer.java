package cg.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String full_name;

    @Column(unique = true, nullable = false)
    private String email;
    private String phone;
    private String address;

    @Column(precision = 12, columnDefinition = "decimal default 0")
    private BigDecimal balance;
    private Date created_at;
    private Long created_by;
    private Date updated_at;
    private Long updated_by;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<Deposit> deposits;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<WithDraw> withdaws;

    public Customer() {
    }

    public Customer(Long id, String full_name, String email, String phone, String address, BigDecimal balance, boolean deleted) {
        this.id = id;
        this.full_name = full_name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.balance = balance;
        this.deleted = deleted;
    }

    public Customer(String full_name, String email, String phone, String address, BigDecimal balance, Date created_at, long created_by, Date updated_at, long updated_by, boolean deleted) {
        this.full_name = full_name;;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.balance = balance;
        this.created_at = created_at;
        this.created_by = created_by;
        this.updated_at = updated_at;
        this.updated_by = updated_by;
        this.deleted = deleted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public long getCreated_by() {
        return created_by;
    }

    public void setCreated_by(long created_by) {
        this.created_by = created_by;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public long getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(long updated_by) {
        this.updated_by = updated_by;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id && created_by == customer.created_by && updated_by == customer.updated_by && deleted == customer.deleted && Objects.equals(full_name, customer.full_name) && Objects.equals(email, customer.email) && Objects.equals(phone, customer.phone) && Objects.equals(address, customer.address) && Objects.equals(balance, customer.balance) && Objects.equals(created_at, customer.created_at) && Objects.equals(updated_at, customer.updated_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, full_name, email, phone, address, balance, created_at, created_by, updated_at, updated_by, deleted);
    }

}

