package com.cg.service;

import com.cg.model.Customer;
import com.cg.repository.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;


@Service
public class CustomerService implements ICustomerService{
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String AMOUNT_REGEX = "^[0-9]+$";
    @Autowired
    private ICustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public List<Customer> findRecipients(Long id) {
        return customerRepository.findRecipients(id);
    }

    @Override
    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void remove(Long id) {
        customerRepository.remove(id);
    }
    @Override
    public void validate(Customer customer, List<String> errors) {

        if (customer.getFull_name().equals("")) {
            errors.add("Full Name không được để trống.");
        }
        if (customer.getEmail().equals("")) {
            errors.add("Email không được để trống.");
        } else {
            if (!Pattern.matches(EMAIL_REGEX, customer.getEmail()) || customer.getEmail().length() > 50) {
                errors.add("Email không hợp lệ.");
            }
        }
        if (customer.getPhone().equals("")) {
            errors.add("Phone không được để trống.");
        }
        if (customer.getAddress().equals("")) {
            errors.add("Address không được để trống.");
        }
    }

    @Override
    public void validateAmount(Long amount, List<String> errors) {
        if (amount.toString().equals("")) {
            errors.add("Amount không được để trống.");
        }else{
            if (!Pattern.matches(AMOUNT_REGEX, amount.toString())) {
                errors.add("Amount phải lớn hơn 0 và nhỏ hơn hoặc bằng 12 chữ số.");
            }
        }
    }
}