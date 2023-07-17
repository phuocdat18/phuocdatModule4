package com.cg.api;

import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.model.dto.customer.CustomerDTO;
import com.cg.model.dto.customer.CustomerReqDTO;
import com.cg.model.dto.customer.CustomerResDTO;
import com.cg.model.dto.deposit.DepositDTO;
import com.cg.model.dto.deposit.DepositReqDTO;
import com.cg.model.dto.locationRegion.LocationRegionReqDTO;
import com.cg.model.dto.transfer.TransferDTO;
import com.cg.model.dto.transfer.TransferReqDTO;
import com.cg.model.dto.withdraw.WithdrawDTO;
import com.cg.model.dto.withdraw.WithdrawReqDTO;
import com.cg.service.customer.ICustomerService;
import com.cg.service.deposit.IDepositService;
import com.cg.service.transfer.ITransferService;
import com.cg.service.withdraw.IWithdrawService;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerAPI {
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IDepositService depositService;

    @Autowired
    private IWithdrawService withdrawService;

    @Autowired
    private ITransferService transferService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ValidateUtils validateUtils;



    @GetMapping
    public ResponseEntity<?> getAllCustomers() {
        List<CustomerDTO> customerDTOS = customerService.findAllCustomersDTO();

        if (customerDTOS.isEmpty()) {
            return new ResponseEntity<>("No customer found.", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(customerDTOS, HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> findById(@PathVariable String customerId) {
        if (!validateUtils.isNumberValid(customerId)) {
            throw new DataInputException("Mã khách hàng không hợp lệ");
        }
        Long id = Long.parseLong(customerId);

        Optional<CustomerDTO> customerOptional = customerService.findCustomerDTOById(id);

        if (!customerOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(customerOptional.get(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCustomer(@Validated @RequestBody CustomerReqDTO customerReqDTO, BindingResult bindingResult) {

        new CustomerReqDTO().validate(customerReqDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Boolean existEmail = customerService.existsByEmailAndDeletedIsFalse(customerReqDTO.getEmail());

        if (existEmail) {
            throw new EmailExistsException("Email đã tồn tại");
        }

        Boolean existsPhone = customerService.existsByPhoneAndDeletedIsFalse(customerReqDTO.getPhone());

        if (existsPhone) {
            throw new EmailExistsException("Phone đã tồn tại");
        }
        CustomerResDTO customerResDTO = customerService.saveNewCustomerFromDTO(customerReqDTO);

        return new ResponseEntity<>(customerResDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/edit/{customerId}")
    public ResponseEntity<?> update(@PathVariable String customerId, @RequestBody CustomerReqDTO customerReqDTO, BindingResult bindingResult) {
        LocationRegionReqDTO locationRegionReqDTO = customerReqDTO.getLocationRegionReqDTO();

        if (!validateUtils.isNumberValid(customerId)) {
            throw new DataInputException("Mã khách hàng không hợp lệ");
        }

        new CustomerReqDTO().validate(customerReqDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Long id = Long.parseLong(customerId);

        Optional<CustomerDTO> customerOptional = customerService.findCustomerDTOById(id);

//        if (!customerOptional.isPresent()) {
//            throw new DataInputException("Mã khách hàng không tồn tại");
//        }
//
//        Boolean existEmail = customerService.existsByEmailAndIdIsNotAndDeletedIsFalse(customerReqDTO.getEmail(), id);
//
//        if (existEmail) {
//            throw new EmailExistsException("Email đã tồn tại");
//        }
//
//        Boolean existsPhone = customerService.existsByPhoneAndIdIsNotAndDeletedIsFalse(customerReqDTO.getPhone(), id);
//
//        if (existsPhone) {
//            throw new EmailExistsException("Phone đã tồn tại");
//        }

        CustomerDTO customerDTO = customerOptional.get();

        CustomerResDTO customerResDTO = customerService.saveUpdatedCustomerFromDTO(customerReqDTO, customerDTO);

        return new ResponseEntity<>(customerResDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String customerId) {
        if (!validateUtils.isNumberValid(customerId)) {
            throw new DataInputException("Mã khách hàng không hợp lệ");
        }
        customerService.suspendCustomer(Long.parseLong(customerId));
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @PatchMapping("/deposits/{customerId}")
    public ResponseEntity<?> deposit(@PathVariable String customerId, @RequestBody DepositReqDTO depositReqDTO, BindingResult bindingResult) {

        if (!validateUtils.isNumberValid(customerId)) {
            throw new DataInputException("Mã khách hàng nộp tiền không hợp lệ");
        }
        new DepositReqDTO().validate(depositReqDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }
        Long id = Long.parseLong(customerId);

        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            throw new DataInputException("Mã khách hàng nộp tiền không tồn tại");
        }
        Customer customer = customerOptional.get();


        BigDecimal newBalance = customer.getBalance().add(depositReqDTO.getTransactionAmount());

        if(newBalance.toString().length() > 12) {
            throw new DataInputException("Vượt quá định mức cho phép. Tổng tiền gửi nhỏ hơn 13 số");
        }

        customer.setBalance(newBalance);

        customerService.save(customer);
        CustomerResDTO customerResDTO = customer.toCustomerResDTO();

        Deposit deposit = depositReqDTO.toDeposit(null, customerResDTO);
        depositService.save(deposit);

        return new ResponseEntity<>(deposit, HttpStatus.OK);
    }



    @PatchMapping("/withdraws/{customerId}")
    public ResponseEntity<?> withdraw(@PathVariable String customerId, @RequestBody WithdrawReqDTO withdrawReqDTO, BindingResult bindingResult) {

        if (!validateUtils.isNumberValid(customerId)) {
            throw new DataInputException("Mã khách hàng rút tiền không hợp lệ");
        }
        new WithdrawReqDTO().validate(withdrawReqDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }
        Long id = Long.parseLong(customerId);

        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            throw new DataInputException("Mã khách hàng rút tiền không tồn tại");
        }
        Customer customer = customerOptional.get();

        BigDecimal newBalance = customer.getBalance().subtract(withdrawReqDTO.getTransactionAmount());

        if(newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new DataInputException("Số dư hiện tại không đủ");
        }

        customer.setBalance(newBalance);

        customerService.save(customer);
        CustomerResDTO customerResDTO = customer.toCustomerResDTO();

        Withdraw withdraw = withdrawReqDTO.toWithdraw(null, customerResDTO);
        withdrawService.save(withdraw);
        return new ResponseEntity<>(withdraw, HttpStatus.OK);
    }


    @PatchMapping("/transfers/{customerId}")
    public ResponseEntity<?> transfer(@PathVariable String customerId, @RequestBody TransferReqDTO transferReqDTO, BindingResult bindingResult) {

        if (!validateUtils.isNumberValid(customerId)) {
            throw new DataInputException("Mã khách chuyển tiền hàng không hợp lệ");
        }
        new TransferReqDTO().validate(transferReqDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }
        Long id = Long.parseLong(customerId);

        Optional<Customer> customerOptionalSender = customerService.findById(id);

        if (!customerOptionalSender.isPresent()) {
            throw new DataInputException("Mã khách hàng chuyển tiền không tồn tại");
        }

        if (!validateUtils.isNumberValid(transferReqDTO.getRecipientId().toString())) {
            throw new DataInputException("Mã khách nhận tiền hàng không hợp lệ");
        }

        Optional<Customer> customerOptionalRecipient = customerService.findById(transferReqDTO.getRecipientId());

        if (!customerOptionalRecipient.isPresent()) {
            throw new DataInputException("Mã khách hàng nhận tiền không tồn tại");
        }

        Customer sender = customerOptionalSender.get();

        Customer recipient = customerOptionalRecipient.get();

        BigDecimal newBalanceSender = sender.getBalance().subtract(transferReqDTO.getTransactionAmount());

        BigDecimal newBalanceRecipient = recipient.getBalance().add(transferReqDTO.getTransferAmount());

        if(newBalanceSender.compareTo(BigDecimal.ZERO) < 0) {
            throw new DataInputException("Số dư hiện tại không đủ để gửi");
        }

        if(newBalanceRecipient.toString().length() > 12) {
            throw new DataInputException("Vượt quá định mức cho phép của người nhận. Tổng định mức nhỏ hơn 13 số");
        }

        sender.setBalance(newBalanceSender);
        Customer newSender = sender;
        customerService.save(newSender);
        CustomerResDTO senderResDTO = newSender.toCustomerResDTO();

        recipient.setBalance(newBalanceRecipient);
        Customer newRecipient = recipient;
        customerService.save(newRecipient);
        CustomerResDTO recipientResDTO = newRecipient.toCustomerResDTO();

        Transfer transfer = transferReqDTO.toTransfer(null, senderResDTO, recipientResDTO);
        transferService.save(transfer);

        return new ResponseEntity<>(transfer.toTransferDTO(), HttpStatus.OK);
    }
}
