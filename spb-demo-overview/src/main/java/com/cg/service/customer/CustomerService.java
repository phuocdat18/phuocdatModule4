package com.cg.service.customer;


import com.cg.model.Customer;
import com.cg.model.LocationRegion;
import com.cg.model.dto.customer.CustomerDTO;
import com.cg.model.dto.customer.CustomerReqDTO;
import com.cg.model.dto.customer.CustomerResDTO;
import com.cg.repository.ICustomerRepository;
import com.cg.service.locationRegion.ILocationRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService implements ICustomerService {

    @Autowired
    private ICustomerRepository customerRepository;

    @Autowired
    private ILocationRegionService locationRegionService;


    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public CustomerResDTO saveNewCustomerFromDTO(CustomerReqDTO customerReqDTO) {
        LocationRegion locationRegion = customerReqDTO.getLocationRegionReqDTO().toLocationRegion(null);
        locationRegionService.save(locationRegion);

        Customer customer = customerReqDTO.toCustomer(null, BigDecimal.ZERO);

        customer.setLocationRegion(locationRegion);

        return save(customer).toCustomerResDTO();
    }

    @Override
    public CustomerResDTO saveUpdatedCustomerFromDTO(CustomerReqDTO customerReqDTO, CustomerDTO customerDTO) {
        LocationRegion locationRegion = customerReqDTO.getLocationRegionReqDTO().toLocationRegion(null);

        locationRegion.setId(customerDTO.getLocationRegionDTO().getId());

        locationRegionService.save(locationRegion);

        Customer newCustomer = customerReqDTO.toCustomer(customerDTO.getId(), customerDTO.getBalance());

        newCustomer.setLocationRegion(locationRegion);

        return save(newCustomer).toCustomerResDTO();
    }

    @Override
    public List<CustomerDTO> findAllCustomersDTO() {
        return customerRepository.findAllCustomerDTO();
    }

    @Override
    public Optional<CustomerDTO> findCustomerDTOById(Long id) {
        return customerRepository.findCustomerDTOById(id);
    }

    @Override
    public List<CustomerDTO> findRecipients(Long senderId) {
        return customerRepository.findRecipients(senderId);
    }

    @Override
    public Optional<Customer> findByIdAndDeletedFalse(Long id) {
        return customerRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public void suspendCustomer(Long id) {
        customerRepository.suspendCustomer(id);
    }

    @Override
    public Boolean existsByEmailAndDeletedIsFalse(String email) {
        return customerRepository.existsByEmailAndDeletedIsFalse(email);
    }

    @Override
    public boolean existsByEmailAndIdIsNotAndDeletedIsFalse(String email, Long id) {
        return customerRepository.existsByEmailAndIdIsNotAndDeletedIsFalse(email, id);
    }

    @Override
    public Boolean existsByPhoneAndDeletedIsFalse(String phone) {
        return customerRepository.existsByPhoneAndDeletedIsFalse(phone);
    }

    @Override
    public boolean existsByPhoneAndIdIsNotAndDeletedIsFalse(String phone, Long id) {
        return customerRepository.existsByPhoneAndIdIsNotAndDeletedIsFalse(phone, id);
    }
}
