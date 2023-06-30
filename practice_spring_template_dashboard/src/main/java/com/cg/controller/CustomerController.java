package com.cg.controller;
import com.cg.model.Customer;
import com.cg.service.customer.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/dashboard")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @GetMapping
    public ModelAndView showListPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("dashboard/list");

        List<Customer> customers = customerService.findAll();

        modelAndView.addObject("customers", customers);

        return modelAndView;
    }

    @GetMapping("/search")
    public String search(@RequestParam String keySearch, Model model) {

        keySearch = "%" + keySearch + "%";

        List<Customer> customers = customerService.findAllByFullNameLikeOrEmailLikeOrPhoneLike(keySearch, keySearch, keySearch);

        model.addAttribute("customers", customers);

        return "dashboard/list";
    }

//    @GetMapping("/info/{id}")
//    public String showInfoPage(@PathVariable Long id, Model model) {
//        ICustomerService customerService = new CustomerServiceImpl();
//
//        Customer customer = customerService.getById(id);
//
//        model.addAttribute("customer", customer);
//
//        return "customer/info";
//    }

    @GetMapping("/info")
    public String showInfoByParamPage(@RequestParam Long id, Model model) {

        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            return "redirect:/errors/404";
        }

        Customer customer = customerOptional.get();

        model.addAttribute("customer", customer);

        return "dashboard/info";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {

        Customer customer = new Customer();

        model.addAttribute("customer", customer);

        return "dashboard/create";
    }

    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable String id, Model model) {
        try {
            Long customerId = Long.parseLong(id);
            Optional<Customer> customerOptional = customerService.findById(customerId);

            if (!customerOptional.isPresent()) {
                return "redirect:/errors/404";
            }

            Customer customer = customerOptional.get();

            model.addAttribute("customer", customer);

            return "dashboard/edit";
        }
        catch (Exception e) {
            return "error/404";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {

        try {
            Long customerId = Long.parseLong(id);
            customerService.deleteById(customerId);

            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Xóa thành công");

            return "redirect:/dashboard";
        }
        catch (Exception e) {
            return "error/404";
        }
    }

    @PostMapping("/create")
    public String doCreate(Customer customer, Model model, RedirectAttributes redirectAttributes) {

        customerService.save(customer);

        model.addAttribute("customer", new Customer());
        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Thêm thành công");

//        return "dashboard/create";
        return "redirect:/dashboard/create";
    }

    @PostMapping("/edit/{id}")
    public String doUpdate(@PathVariable Long id, @ModelAttribute Customer customer, Model model, RedirectAttributes redirectAttributes) {

        customer.setId(id);
        customerService.save(customer);

        List<Customer> customers = customerService.findAll();

        model.addAttribute("customers", customers);
        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Cập nhật thành công");

        return "redirect:/dashboard";
    }


    @GetMapping("/deposit/{id}")
    public ModelAndView showDeposit(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("dashboard/deposit");
        Customer customer = customerService.findById(id).get();
        modelAndView.addObject("customer", customer);
        return modelAndView;
    }

    @PostMapping("/deposit/{id}")
    public ModelAndView depositAmount(@ModelAttribute Customer customer, @RequestParam("deposit") long depositAmount) {
        List<String> errors = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("/dashboard/deposit");
        customerService.validateMount(depositAmount, errors);
        Customer customerDeposit = customerService.findById(customer.getId()).get();
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
        ModelAndView modelAndView = new ModelAndView("/dashboard/withdraw");
        Customer customer = customerService.findById(id).get();
        modelAndView.addObject("customer", customer);
        return modelAndView;
    }
    @PostMapping("/withdraw/{id}")
    public ModelAndView withDrawAmount(@ModelAttribute Customer customer, @RequestParam("withdraw") long withdrawAmount) {
        List<String> errors = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView("/dashboard/withdraw");
        customerService.validateAmount(withdrawAmount, errors);
        Customer customerWithDraw = customerService.findById(customer.getId()).get();
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

}