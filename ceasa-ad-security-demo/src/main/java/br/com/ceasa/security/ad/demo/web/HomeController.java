package br.com.ceasa.security.ad.demo.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication auth, Model model) {
        model.addAttribute("usuario", auth.getName());
        model.addAttribute("authorities", auth.getAuthorities());
        return "home";
    }
}
