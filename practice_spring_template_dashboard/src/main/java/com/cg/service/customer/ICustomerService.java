package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.service.IGeneralService;

import java.util.List;


public interface ICustomerService extends IGeneralService<Customer, Long> {

    List<Customer> findAllByFullNameLike(String fullName);

    List<Customer> findAllByFullNameLikeOrEmailLikeOrPhoneLike(String fullName, String email, String phone);

    void validate(Customer customer, List<String> errors);

    void validateMount(long depositAmount, List<String> errors);

    void validateAmount(long withdrawAmount, List<String> errors);
}