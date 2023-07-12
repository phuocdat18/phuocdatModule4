package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.model.dto.CustomerDTO;
import com.cg.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;

public interface ICustomerService extends IGeneralService<Customer, Long> {
    Boolean existsByEmailAndIdNot(String email, Long id);

    Boolean existsByEmail(String email);

    Transfer transfer(Transfer transfer);

    public List<Customer> findAllByIdNotAndDeletedIsFalse(Long id);


    boolean existsByEmailAndIdIsNot(String email, Long id);

    Boolean existById(Long id);

    Customer deposit(Deposit deposit);

    Customer withdraw(Withdraw withdraw);

    void incrementBalance(Long id, BigDecimal amount);

    List<Customer> findAllByDeletedIsFalse();
    void decrementBalance(Long id, BigDecimal amount);
}