package com.hiveelpay.mgr.controller;

import com.google.common.base.Strings;
import com.hiveelpay.HiveelConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @Autowired
    private HiveelConfig hiveelConfig;

    @GetMapping(value = {
            "/",
            "/home.html",
            "/index.html"
    })
    public String index() {
        return "/index";
    }

    @GetMapping("/login.html")
    public String login() {
        return "/login";
    }

    @PostMapping("/dologin")
    public ModelAndView doLogin(HttpServletRequest request, HttpServletResponse response, String userName, String password) {
        ModelAndView mav = new ModelAndView("redirect:/index.html");
        if (Strings.isNullOrEmpty(userName) || Strings.isNullOrEmpty(password) || !(userName.equalsIgnoreCase(hiveelConfig.getUsername()) && password.equals(hiveelConfig.getPassword()))) {
            mav = new ModelAndView("/login");
            mav.addObject("msg", "Please type valid userName and password!");
            return mav;
        }
        request.getSession().setAttribute("user", hiveelConfig.getUsername());
        return mav;
    }

    @GetMapping("/logout.html")
    public String logout(HttpServletRequest request) {
        request.getSession().setAttribute("user", null);
        return "redirect:/login.html";
    }
}
