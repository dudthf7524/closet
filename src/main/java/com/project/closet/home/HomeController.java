package com.project.closet.home;

import com.project.closet.member.CustomUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(){

        return"index";
    }
}
