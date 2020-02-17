package com.yzt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/system")
public class SystemController {
    @RequestMapping("toUserManage")
    public String toUserManage() {
        return "/system/userManage";
    }

    @RequestMapping("toBackUp")
    public String toBackUp() {
        return "/system/backup";
    }
}
