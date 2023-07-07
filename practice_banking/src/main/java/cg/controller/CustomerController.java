package cg.controller;

import cg.model.Transfer;
import cg.service.ICustomerService;
import cg.model.Customer;
import cg.service.ITransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class CustomerController {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ITransferService transferService;
    @GetMapping("/customers")
    public ModelAndView showList() {
        ModelAndView modelAndView = new ModelAndView("/customers/list");
        List<Customer> customers = customerService.findAll();
        modelAndView.addObject("customers", customers);
        return modelAndView;
    }
    @GetMapping("/customers/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/customers/create");
        Customer newCustomer = new Customer();
        modelAndView.addObject("newCustomer", newCustomer);
        return modelAndView;
    }
    @PostMapping("/customers/create")
    public ModelAndView createCustomer(@ModelAttribute("customer") Customer customer) {
        List<String> errors = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("/customers/create");
        customerService.validate(customer,errors);
        if (errors.isEmpty()) {
         customerService.save(customer);
         messages.add("New customer created successfully");
        }
        modelAndView.addObject("newCustomer", new Customer());
        modelAndView.addObject("messages", messages);
        modelAndView.addObject("errors", errors);
        return modelAndView;
    }
    @GetMapping("/customers/edit/{id}")
    public ModelAndView showEditForm(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("/customers/edit");
        Customer customer = customerService.findById(id);
        modelAndView.addObject("currentCustomer", customer);
        return modelAndView;
    }

    @PostMapping("/customers/edit/{id}")
    public ModelAndView updateCustomer(@PathVariable long id, @ModelAttribute Customer currentCustomer) {
        List<String> errors = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("/customers/edit");
        customerService.validate(currentCustomer,errors);
        if (errors.isEmpty()) {
            customerService.save(currentCustomer);
            messages.add("Customer update successfully");
        }
        modelAndView.addObject("currentCustomer", new Customer());
        modelAndView.addObject("messages", messages);
        modelAndView.addObject("errors", errors);
        return modelAndView;
    }

    @GetMapping("/customers/suspend/{id}")
    public ModelAndView showSuspendForm(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("/customers/suspend");
        Customer customer = customerService.findById(id);
        modelAndView.addObject("currentCustomer", customer);
        return modelAndView;
    }

    @PostMapping("/customers/suspend/{id}")
    public ModelAndView suspendCustomer(@PathVariable long id, @ModelAttribute Customer currentCustomer) {
        List<String> messages = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("/customers/suspend");
        currentCustomer.setDeleted(true);
        customerService.save(currentCustomer);
        messages.add("Customer suspend successfully");
        modelAndView.addObject("currentCustomer", new Customer());
        modelAndView.addObject("messages", messages);
        return modelAndView;
    }
    @GetMapping("/deposit/{id}")
    public ModelAndView showDeposit(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("/customers/deposit");
        Customer customer = customerService.findById(id);
        modelAndView.addObject("customer", customer);
        return modelAndView;
    }

    @PostMapping("/deposit/{id}")
    public ModelAndView depositAmount(@ModelAttribute Customer customer, @RequestParam("deposit") long depositAmount) {
        List<String> errors = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("/customers/deposit");
        customerService.validateAmount(depositAmount, errors);
        Customer customerDeposit = customerService.findById(customer.getId());
        if (errors.isEmpty()) {
            BigDecimal balance = customerDeposit.getBalance().add(BigDecimal.valueOf(depositAmount));
            if(balance.toString().length() > 12) {
                errors.add("Vượt quá định mức tiền gửi. Tổng tiền gửi nhỏ hơn 12 chữ số.");
            }else {
                customerDeposit.setBalance(balance);
                customerService.save(customerDeposit);
                messages.add("Customer update successfully");
            }
        }

        modelAndView.addObject("customer", customerDeposit);
        modelAndView.addObject("messages", messages);
        modelAndView.addObject("errors", errors);
        return modelAndView;
    }
    @GetMapping("/withdraw/{id}")
    public ModelAndView showWithDraw(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("/customers/withdraw");
        Customer customer = customerService.findById(id);
        modelAndView.addObject("customer", customer);
        return modelAndView;
    }
    @PostMapping("/withdraw/{id}")
    public ModelAndView withDrawAmount(@ModelAttribute Customer customer, @RequestParam("withdraw") long withdrawAmount) {
        List<String> errors = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("/customers/withdraw");
        customerService.validateAmount(withdrawAmount, errors);
        Customer customerWithDraw = customerService.findById(customer.getId());
        if (errors.isEmpty()) {
            if(customerWithDraw.getBalance().compareTo(BigDecimal.valueOf(withdrawAmount)) < 0) {
                errors.add("Số dư hiện tại không đủ. Vui lòng rút số tiền nhỏ hơn " + customerWithDraw.getBalance());
            }else {
                BigDecimal balance = customerWithDraw.getBalance().subtract(BigDecimal.valueOf(withdrawAmount));
                customerWithDraw.setBalance(balance);
                customerService.save(customerWithDraw);
                messages.add("Customer update successfully");
            }

        }
        modelAndView.addObject("customer", customerWithDraw);
        modelAndView.addObject("messages", messages);
        modelAndView.addObject("errors", errors);
        return modelAndView;
    }
    @GetMapping("/transfer/{id}")
    public ModelAndView showTransfer(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("/customers/transfer");
        Customer sender = customerService.findById(id);
        List<Customer> recipients = customerService.findRecipients(id);
        modelAndView.addObject("sender", sender);
        modelAndView.addObject("recipients", recipients);
        return modelAndView;
    }
    @PostMapping("/transfer/{id}")
    public ModelAndView transfer(@PathVariable long id, @RequestParam("recipientId") long recipientId, @RequestParam("fees") long fees, @RequestParam("transfer") long transferAmount) {
        List<String> errors = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("/customers/transfer");
        Customer customerSender = customerService.findById(id);
        Customer customerRecipient = customerService.findById(recipientId);
        customerService.validateAmount(transferAmount,errors);
        if(errors.isEmpty()) {
            long fees_amount = (fees * transferAmount) / 100;
            long transaction_amount = transferAmount + fees_amount;

            if(customerSender.getBalance().compareTo(BigDecimal.valueOf(transaction_amount)) < 0) {
                errors.add("Số dư hiện tại không đủ. Vui lòng chuyển số tiền nhỏ hơn " + customerSender.getBalance().subtract(BigDecimal.valueOf(fees_amount)));
            }else {
                BigDecimal balanceSender = customerSender.getBalance().subtract(BigDecimal.valueOf(transaction_amount));
                BigDecimal balanceRecipent = customerRecipient.getBalance().add(BigDecimal.valueOf(transferAmount));
                if(balanceRecipent.toString().length() > 12) {
                    errors.add("Vượt quá định mức tổng tiền người nhận. Tổng tiền gửi nhỏ hơn 12 chữ số.");
                }else {
                    customerSender.setBalance(balanceSender);
                    customerRecipient.setBalance(balanceRecipent);
                    //BigDecimal fees, BigDecimal fees_amount, BigDecimal transaction_amount, BigDecimal transfer_amount, Long recipient_id, Long sender_id
                    Transfer transfer = new Transfer(BigDecimal.valueOf(fees), BigDecimal.valueOf(fees_amount), BigDecimal.valueOf(transaction_amount), BigDecimal.valueOf(transferAmount), recipientId, id);
                    customerService.save(customerSender);
                    customerService.save(customerRecipient);
                    transferService.save(transfer);
                    messages.add("Customer update successfully");
                }
            }
        }
        List<Customer> recipients = customerService.findRecipients(id);
        modelAndView.addObject("sender", customerSender);
        modelAndView.addObject("recipients", recipients);
        modelAndView.addObject("messages", messages);
        modelAndView.addObject("errors", errors);
        return modelAndView;
    }
    @GetMapping("/transferInfo")
    public ModelAndView showTransfer() {
        ModelAndView modelAndView = new ModelAndView("/customers/listTransfer");
        List<Transfer> transfers = transferService.findAll();
        modelAndView.addObject("transfers", transfers);
        return modelAndView;
    }
}
