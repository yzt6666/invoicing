package com.yzt.controller;

import com.yzt.entity.UserList;
import com.yzt.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Resource
    private UserService userService;

    @RequestMapping({"/index","/"})
    public String toIndex() {
        System.out.println("index");
        return "/mainPage";
    }

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "/index";
    }

    @RequestMapping("/login")
    public String login(String username, String password, Model model, HttpSession session) {
        //获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);//执行登录的方法，如果没有异常就说明成功
            UserList user = (UserList) subject.getPrincipal();
            return "/mainPage";
        }catch (UnknownAccountException e){ //用户名不存在
            model.addAttribute("msg", "用户名不存在");
            return "/index";
        }catch (IncorrectCredentialsException e){ //密码错误
            model.addAttribute("msg", "密码错误");
            return "/index";
        }

    }

}