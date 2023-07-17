package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.dto.customer.CustomerDTO;
import com.cg.model.dto.customer.CustomerReqDTO;
import com.cg.model.dto.customer.CustomerResDTO;
import com.cg.service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface ICustomerService extends IGeneralService<Customer, Long> {
    CustomerResDTO saveNewCustomerFromDTO(CustomerReqDTO customerReqDTO);

    CustomerResDTO saveUpdatedCustomerFromDTO(CustomerReqDTO customerReqDTO, CustomerDTO customerDTO);

    List<CustomerDTO> findAllCustomersDTO();

    Optional<CustomerDTO> findCustomerDTOById(Long id);

    List<CustomerDTO> findRecipients(Long senderId);

    Optional<Customer> findByIdAndDeletedFalse(Long id);

    void suspendCustomer(Long id);
    Boolean existsByEmailAndDeletedIsFalse(String email);
    boolean existsByEmailAndIdIsNotAndDeletedIsFalse(String email, Long id);
    Boolean existsByPhoneAndDeletedIsFalse(String phone);
    boolean existsByPhoneAndIdIsNotAndDeletedIsFalse(String phone, Long id);
}
