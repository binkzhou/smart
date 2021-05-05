package com.potato.smart.controller;


import com.potato.smart.entity.User;
import com.potato.smart.mapper.UserDeviceMapper;
import com.potato.smart.utils.JwtUtils;
import com.potato.smart.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.Cacheable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-04-20
 */
@RestController
@RequestMapping("/smart/user-device")
@CrossOrigin
public class UserDeviceController {
    @Autowired
    private UserDeviceMapper userDeviceMapper;
    //根据token获取用户信息
    @GetMapping("/getDeviceList")
    public R getDeviceList(HttpServletRequest request) {
        //调用jwt工具类的方法。根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库根据用户id获取用户信息
        List<Map<String, String>> userDeviceList = userDeviceMapper.getUserDeviceList(memberId);
        return R.ok().data("deviceList",userDeviceList);
    }
}

