package cg.service;


import cg.model.Customer;

import java.math.BigDecimal;
import java.util.List;

public interface ICustomerService extends IGenaralService<Customer> {
    void validate(Customer customer, List<String> errors);
    void validateAmount(Long amount, List<String> errors);
}
