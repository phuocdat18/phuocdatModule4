package com.cg.model.dto;

import com.cg.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerUpdateReqDTO implements Validator {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;

    public Customer toCustomer(){
        Customer customer  = new Customer();
        customer.setFullName(fullName);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);
        return customer;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CustomerUpdateReqDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CustomerUpdateReqDTO customerUpdateReqDTO = (CustomerUpdateReqDTO) target;

         String fullName = customerUpdateReqDTO.fullName;
        String email = customerUpdateReqDTO.email;

         if (fullName == null) {
            errors.rejectValue("fullName", "fullName.null", "Họ tên là bắt buộc");
        }
        else {
            if (fullName.trim().length() == 0) {
                errors.rejectValue("fullName", "fullName.empty", "Họ tên là không được để trống");
            }
            else {
                if (fullName.trim().length() > 50 || fullName.trim().length() < 5) {
                    errors.rejectValue("fullName", "fullName.length.min-max", "Họ tên phải từ 5-50 ký tự");
                }
            }
        }

        if (email == null) {
            errors.rejectValue("email", "email.null", "Email là bắt buộc");
        }
        else {
            if (email.trim().length() == 0) {
                errors.rejectValue("email", "email.empty", "Email là không được để trống");
            }
            else {
                if (!email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                    errors.rejectValue("email", "email.match", "Email chưa đúng định dạng");
                }
            }
        }
    }
}
