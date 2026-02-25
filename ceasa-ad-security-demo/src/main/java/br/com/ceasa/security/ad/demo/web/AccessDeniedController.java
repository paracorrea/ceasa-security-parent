package br.com.ceasa.security.ad.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {

    @GetMapping("/sem-permissao")
    public String semPermissao() {
        return "sem-permissao";
    }
}
