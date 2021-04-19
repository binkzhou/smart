package com.potato.smart.controller;

import com.potato.smart.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/login")
    public R test(){
        return R.ok().message("登录成功");
    }
}
