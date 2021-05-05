package com.potato.smart.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.potato.smart.entity.User;
import com.potato.smart.entity.vo.UserVo;
import com.potato.smart.mapper.UserMapper;
import com.potato.smart.service.MailService;
import com.potato.smart.utils.JwtUtils;
import com.potato.smart.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author zhoubin
 * @since 2021-04-19
 */
@RestController
@RequestMapping("/smart/user")
@CrossOrigin
public class UserController {
    @Autowired
    private MailService mailService;
    @Autowired(required=false)
    private UserMapper userMapper;//注入UserMapper，交给bena

    @GetMapping("/sendEmail")
    public R sendEmail(String email,Boolean IsUpdatePassWord, HttpSession httpSession){
        QueryWrapper w = new QueryWrapper();
        w.eq("email",email);
        User user = userMapper.selectOne(w);
        if(user!=null&& !IsUpdatePassWord){
            return R.error().message("该邮箱已注册");
        }else{
            boolean isSuccess = mailService.sendMimeMail(email, httpSession);
            if(isSuccess){
                return R.ok().message("邮件发送成功");
            }else{
                return R.error().message("邮件发送失败");
            }
        }
    }
    @PostMapping("/login")
    public R login(@RequestBody User userInfo){
        QueryWrapper w = new QueryWrapper();
        w.eq("email",userInfo.getEmail());
        User user = userMapper.selectOne(w);
        if(user!=null){
            if(!user.getPassword().equals(userInfo.getPassword())){
                return R.error().message("邮箱或密码错误");
            }
        }else{
            return R.error().message("邮箱没有注册");
        }
        String jwtToken = JwtUtils.getJwtToken(user.getId(), user.getUsername());
        return R.ok().data("jwtToken",jwtToken);
    }

    @PostMapping("/register")
    @ResponseBody
    public R register(@RequestBody UserVo userVo, HttpSession session){
        String email = (String) session.getAttribute("email");
        String code = (String) session.getAttribute("code");

        //获取表单中的提交的验证信息
        String voCode = userVo.getCode();
        //如果email数据为空，或者不一致，注册失败
        if (email == null || email.isEmpty()){
            return R.error().message("注册失败");
        }else if (!code.equals(voCode)){
            return R.ok().message("验证码错误，请重新输入");
        }
        //保存数据
        User user = new User();
        user.setUsername(userVo.getUsername());
        user.setPassword(userVo.getPassword());
        user.setEmail(userVo.getEmail());
        //将数据写入数据库
        userMapper.insert(user);
        return R.ok().message("注册成功");
    }

    @PostMapping("/rest")
    @ResponseBody
    public R rest(@RequestBody UserVo userVo, HttpSession session){
        String email = (String) session.getAttribute("email");
        String code = (String) session.getAttribute("code");
        //获取表单中的提交的验证信息
        String voCode = userVo.getCode();
        System.out.println(email);
        //如果email数据为空，或者不一致，注册失败
        if (email == null || email.isEmpty()){
            return R.error().message("重置失败");
        }else if (!code.equals(voCode)){
            return R.ok().message("验证码错误，请重新输入");
        }
        //保存数据
        QueryWrapper w = new QueryWrapper();
        w.eq("email",userVo.getEmail());
        User user = new User();
        user.setPassword(userVo.getPassword());
        //将数据写入数据库
        int update = userMapper.update(user, w);
        if(update>=1){
            return R.ok().message("重置成功");
        }else{
            return R.error().message("重置失败");
        }
    }



    //根据token获取用户信息
    @GetMapping("/getUserInfo")
    public R getUserInfo(HttpServletRequest request) {
        //调用jwt工具类的方法。根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库根据用户id获取用户信息
        User user = userMapper.selectById(memberId);
        user.setPassword("");
        return R.ok().data("userInfo",user);
    }
}

