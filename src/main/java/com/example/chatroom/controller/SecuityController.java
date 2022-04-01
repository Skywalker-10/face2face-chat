package com.example.chatroom.controller;

import com.example.chatroom.model.vo.ResponseJson;
import com.example.chatroom.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

public class SecuityController {


    SecurityService securityService;

    @RequestMapping(value = {"login","/"},method = RequestMethod.GET)
    public String toLogin(){ return "login";}

    @RequestMapping(value = "login",method = RequestMethod.POST)
    @ResponseBody
    public ResponseJson login(HttpSession session, @RequestParam String username,@RequestParam String password){
        return securityService.login(username,password,session);
    }

    @RequestMapping(value = "logout",method = RequestMethod.POST)
    @ResponseBody
    public ResponseJson logout(HttpSession session){
        return securityService.logout(session);
    }
}
