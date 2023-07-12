package com.cg.model;


import com.cg.model.dto.CustomerDTO;
import com.cg.model.dto.CustomerResDTO;
import com.cg.model.dto.CustomerUpdateResDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customers")
@Accessors(chain = true)
public class Customer extends BasseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private String address;

    @Column(precision = 10, scale = 0, nullable = false)
//    , updatable = false
    private BigDecimal balance;


    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Deposit> deposits;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Withdraw> withdraws;

    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private List<Transfer> senders;

    @OneToMany(mappedBy = "recipient")
    @JsonIgnore
    private List<Transfer> recipients;

    public CustomerResDTO toCustomerResDTO() {
        return new CustomerResDTO()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setBalance(balance)
                .setAddress(address)
                ;
    }

    public CustomerDTO toCustomerDTO() {
        return new CustomerDTO()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setBalance(balance)
                .setAddress(address)
                ;
    }
    public CustomerUpdateResDTO toCustomerUpdateResDTO() {
        return new CustomerUpdateResDTO()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setBalance(balance)
                .setAddress(address)
                ;
    }

}