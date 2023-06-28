package com.cg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

// thymeleaf giúp code gọn gàng, hỗ trợ cắt giao diện, tái sự dụng code


@Controller
@RequestMapping("/customers")    //Mặc định là get, hỗ trợ class và function
public class CustomerController {
//    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    @GetMapping    // hỗ trợ function
    public ModelAndView showListPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/list");

        modelAndView.addObject("fullname", "ABC");

        return modelAndView;

//        return "/customer/list";
    }

//    @RequestMapping(value = "/customers/infor", method = RequestMethod.GET)
    @GetMapping("/infor")
    public String showInforPage() {
        return "customer/infor";
    }

//    @RequestMapping(value = "/customers/create", method = RequestMethod.GET)
    @GetMapping("/create")
    public String showCreatePage() {
        return "customer/create";
    }
}
