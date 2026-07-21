package com.example.peminjamanbaju.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BajuAdatPageController {
    @GetMapping({
            "/baju-adat",
            "/baju-adat/",
            "/baju-adat/catalog",
            "/baju-adat/form"
    })
    public String forwardBajuAdatPage() {
        return "forward:/baju-adat/index.html";
    }
}
