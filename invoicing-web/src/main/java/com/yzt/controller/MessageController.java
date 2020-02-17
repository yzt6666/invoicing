package com.yzt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/message")
public class MessageController {
    @RequestMapping("toGoodsMessage")
    public String toGoodsMessage() {
        return "/message/goodsMessage";
    }

    @RequestMapping("toEmployeeMessage")
    public String toEmployeeMessage() {
        return "/message/employeeMessage";
    }
}
