package com.cg.api;


import com.cg.exception.DataInputException;
import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.dto.CustomerResDTO;
import com.cg.model.dto.DepositCreReqDTO;
import com.cg.model.dto.TransferReqDTO;
import com.cg.service.customer.ICustomerService;
import com.cg.service.transfer.ITransferService;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/transfers")
public class TransferAPI {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ValidateUtils validateUtils;

    @Autowired
    private ITransferService transferService;

    @PatchMapping("/{customerId}")
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
