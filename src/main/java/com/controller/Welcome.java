package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Song on 2017/5/23.
 */
@Controller
public class Welcome {
    @RequestMapping("/")
    public String welcome()
    {
        return "index";
    }
}
