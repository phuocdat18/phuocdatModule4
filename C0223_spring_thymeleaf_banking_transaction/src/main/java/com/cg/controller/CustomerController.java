package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.service.customer.ICustomerService;
import com.cg.service.depossit.IDepositService;
//import com.cg.service.transfer.ITransferService;
import com.cg.service.transfer.ITransferService;
import com.cg.service.withdraw.IWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IDepositService depositService;
    @Autowired
    private IWithdrawService withdrawService;
    @Autowired
    private ITransferService transferService;

    @GetMapping
    public String showListPage(Model model) {
        List<Customer> customers = customerService.findAll();

        model.addAttribute("customers", customers);

        return "customer/list";
    }

    @GetMapping("/history")
    public String showListHistory(Model model) {
        List<Customer> customerOptional = customerService.findAll();

            Customer customer = (Customer) customerOptional;
            Deposit deposit = new Deposit();
            deposit.setCustomer(customer);
            model.addAttribute("deposit", deposit);

        return "customer/history";
    }


    @GetMapping("/create")
    public String showCreatePage() {
        return "customer/create";
    }

    @GetMapping("/deposit/{customerId}")
    public String showDepositPage(@PathVariable Long customerId, Model model) {

        Optional<Customer> customerOptional = customerService.findById(customerId);
        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Id khách hàng không tồn tại");
        } else {
            Customer customer = customerOptional.get();
            Deposit deposit = new Deposit();
            deposit.setCustomer(customer);
            model.addAttribute("deposit", deposit);
        }

        return "customer/deposit";
    }

    @GetMapping("/withdraw/{customerId}")
    public String showWithdrawPage(@PathVariable Long customerId, Model model) {

        Optional<Customer> customerOptional = customerService.findById(customerId);
        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Id khách hàng không tồn tại");
        } else {
            Customer customer = customerOptional.get();
            Withdraw withdraw = new Withdraw();
            withdraw.setCustomer(customer);
            model.addAttribute("withdraw", withdraw);
        }

        return "customer/withdraw";
    }

    @GetMapping("/suspended/{customerId}")
    public ModelAndView showSuspendForm(@PathVariable Long customerId) {
        ModelAndView modelAndView = new ModelAndView("/customer/suspended");
        Customer customer = customerService.findById(customerId).get();
        modelAndView.addObject("currentCustomer", customer);
        return modelAndView;
    }

    @GetMapping("/edit/{customerId}")
    public String showEditPage(@PathVariable Long customerId, Model model) {
        try {
            Optional<Customer> customerOptional = customerService.findById(customerId);

            if (!customerOptional.isPresent()) {
                return "redirect:/error.404";
            }

            Customer customer = customerOptional.get();

            model.addAttribute("customer", customer);

            return "customer/edit";
        }
        catch (Exception e) {
            return "error404";
        }
    }

    @GetMapping("/transfer/{customerId}")
    public ModelAndView showTransfer(@PathVariable Long customerId) {
        ModelAndView modelAndView = new ModelAndView("/customer/transfer");
        Customer sender = customerService.findById(customerId).get();
        List<Customer> recipients = customerService.findRecipients(customerId);
        modelAndView.addObject("sender", sender);
        modelAndView.addObject("recipients", recipients);
        return modelAndView;
    }


    @PostMapping("/create")
    public String doCreate(@ModelAttribute Customer customer) {
        customer.setId(null);
        customer.setBalance(BigDecimal.ZERO);
        customerService.save(customer);
        return "customer/create";
    }

    @PostMapping("/deposit/{customerId}")
    public String doDeposit(@ModelAttribute Deposit deposit, @PathVariable Long customerId, Model model) {

        Optional<Customer> customerOptional = customerService.findById(customerId);
        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Id khách hàng không tồn tại");
        } else {

            Customer customer = customerOptional.get();
            deposit.setId(null);
            depositService.save(deposit);

            BigDecimal currentBalance = customer.getBalance();
            BigDecimal newBalance = currentBalance.add(deposit.getTransactionAmount());
            customer.setBalance(newBalance);
            customerService.save(customer);

            deposit.setCustomer(customer);

            model.addAttribute("deposit", deposit);
        }

        return "customer/deposit";
    }

    @PostMapping("/withdraw/{customerId}")
    public String doWithdraw(@ModelAttribute Withdraw withdraw, @PathVariable Long customerId, Model model) {

        Optional<Customer> customerOptional = customerService.findById(customerId);
        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Id khách hàng không tồn tại");
        } else {
            Customer customer = customerOptional.get();
            withdraw.setId(null);
            withdraw.setTransactionAmount(withdraw.getTransactionAmount());

            BigDecimal currentBalance = customer.getBalance();
            if (currentBalance.compareTo(withdraw.getTransactionAmount()) < 0) {
                model.addAttribute("error", true);
                model.addAttribute("message", "Số dư không đủ để rút tiền");
            } else {
                BigDecimal newBalance = currentBalance.subtract(withdraw.getTransactionAmount());
                customer.setBalance(newBalance);
                customerService.save(customer);

                withdraw.setCustomer(customer);
                withdrawService.save(withdraw);

                model.addAttribute("withdraw", withdraw);
            }
        }

        return "customer/withdraw";
    }

//    @PostMapping("/suspended/{customerId}")
//    public ModelAndView suspendCustomer(@PathVariable Long customerId, @ModelAttribute Customer currentCustomer) {
//        List<String> messages = new ArrayList<>();
//        ModelAndView modelAndView = new ModelAndView("/customer/list");
//        currentCustomer.setDeleted(true);
//        customerService.save(currentCustomer);
//        messages.add("Customer suspend successfully");
//        modelAndView.addObject("currentCustomer", new Customer());
//        modelAndView.addObject("messages", messages);
//        return modelAndView;
//    }


    @PostMapping("/suspended/{customerId}")
    public String suspendCustomer(@PathVariable Long customerId, RedirectAttributes redirectAttributes) {
        Optional<Customer> optionalCustomer = customerService.findById(customerId);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setDeleted(true);
            customerService.save(customer);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Khách hàng đã bị tạm hoãn thành công");
        } else {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy khách hàng");
        }

        return "redirect:/customers";
    }


    @PostMapping("/edit/{customerId}")
    public String doUpdate(@PathVariable Long customerId, @ModelAttribute Customer customer, Model model, RedirectAttributes redirectAttributes) {

        customer.setId(customerId);
        customerService.save(customer);

        List<Customer> customers = customerService.findAll();

        model.addAttribute("customers", customers);
        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Cập nhật thành công");

        return "redirect:/customers";
    }

    @PostMapping("/transfer/{customerId}")
    public ModelAndView transfer(@PathVariable Long customerId, @RequestParam("recipientId") long recipientId, @RequestParam("fees") long fees, @RequestParam("transfer") long transferAmount) {
        List<String> errors = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("/customer/transfer");
        Customer customerSender = customerService.findById(customerId).get();
        Customer customerRecipient = customerService.findById(recipientId).get();
        if (errors.isEmpty()) {
            long fees_amount = (fees * transferAmount) / 100;
            long transaction_amount = transferAmount + fees_amount;

            if (customerSender.getBalance().compareTo(BigDecimal.valueOf(transaction_amount)) < 0) {
                errors.add("Số dư không đủ" + customerSender.getBalance().subtract(BigDecimal.valueOf(fees_amount)));
            } else {
                BigDecimal balanceSender = customerSender.getBalance().subtract(BigDecimal.valueOf(transaction_amount));
                BigDecimal balanceRecipent = customerRecipient.getBalance().add(BigDecimal.valueOf(transferAmount));
                if (balanceRecipent.toString().length() > 12) {
                    errors.add("Tổng tiền gửi nhỏ hơn 12 chữ số.");
                } else {
                    customerSender.setBalance(balanceSender);
                    customerRecipient.setBalance(balanceRecipent);
                    //BigDecimal fees, BigDecimal fees_amount, BigDecimal transaction_amount, BigDecimal transfer_amount, Long recipient_id, Long sender_id
                    Transfer transfer = new Transfer(BigDecimal.valueOf(fees), BigDecimal.valueOf(fees_amount), BigDecimal.valueOf(transaction_amount), BigDecimal.valueOf(transferAmount), recipientId, customerId);
                    customerService.save(customerSender);
                    customerService.save(customerRecipient);
                    transferService.save(transfer);
                    messages.add("Customer update successfully");
                }
            }
        }
        List<Customer> recipients = customerService.findRecipients(customerId);
        modelAndView.addObject("sender", customerSender);
        modelAndView.addObject("recipients", recipients);
        modelAndView.addObject("messages", messages);
        modelAndView.addObject("errors", errors);
        return modelAndView;
    }
}
