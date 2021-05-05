package com.potato.smart.service;

import com.potato.smart.entity.vo.UserVo;

import javax.servlet.http.HttpSession;

public interface MailService {
    public boolean sendMimeMail( String email, HttpSession session);
    public boolean registered(UserVo userVo, HttpSession session);
}
