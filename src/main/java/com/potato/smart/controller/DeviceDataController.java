package com.potato.smart.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.potato.smart.entity.Device;
import com.potato.smart.entity.DeviceData;
import com.potato.smart.entity.User;
import com.potato.smart.entity.UserDevice;
import com.potato.smart.mapper.DeviceDataMapper;
import com.potato.smart.mapper.DeviceMapper;
import com.potato.smart.service.UserDeviceService;
import com.potato.smart.service.UserService;
import com.potato.smart.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSON;

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
@RequestMapping("/smart/device-data")
@CrossOrigin
public class DeviceDataController {

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceDataMapper deviceDataMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private JavaMailSender mailSender;//一定要用@Autowired

    @PostMapping("/insertDeviceData")
    public void insertDeviceData(@RequestBody Map<String, Object> param) {
        System.out.println(param);
        DeviceData deviceData = JSON.parseObject(JSON.toJSONString(param), DeviceData.class);
        Map map = JSON.parseObject(deviceData.getData(), Map.class);
        if(map.get("fire")!=null){
            String fire = (String)map.get("fire");
            if(fire.equals("1")){
                System.out.println("666");
                String device_sn = param.get("sn").toString();
                QueryWrapper w = new QueryWrapper();
                w.eq("sn", device_sn);
                Device device = deviceMapper.selectOne(w);
                QueryWrapper userDevicemapper = new QueryWrapper();
                userDevicemapper.eq("did",device.getId());
                UserDevice one = userDeviceService.getOne(userDevicemapper);

                User user = userService.getById(one.getUid());
                System.out.println(user);
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setSubject("警告！");//主题
                mailMessage.setText("发生火灾了");
                mailMessage.setTo(user.getEmail());//发给谁
                mailMessage.setFrom("525589458@qq.com");
                mailSender.send(mailMessage);//发送
            }
        }
        deviceDataMapper.insert(deviceData);
    }

    @GetMapping("/getDeviceData")
    private R getDeviceData(String sn){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("sn",sn);
        List list = deviceDataMapper.selectList(wrapper);
        return R.ok().data("deviceData",list);
    }

    @GetMapping("/getDeviceNewData")
    private R getDeviceNewData(String sn){
        Map deviceNewData = deviceDataMapper.getDeviceNewData(sn);
        return R.ok().data("deviceNewData",deviceNewData);
    }

    @PostMapping("/updateDeviceState")
    public void updateDeviceState(@RequestBody Map<String, String> param) {
        System.out.println(param);
        String action = param.get("action");
        String clientId = param.get("clientid");
        QueryWrapper wrapper = new QueryWrapper();
        Device device = new Device();
        wrapper.eq("sn",clientId);
        if(action.equals("client_connected")){
            device.setStatus(1);
            deviceMapper.update(device,wrapper);
        }else if(action.equals("client_disconnected")){
            device.setStatus(0);
            deviceMapper.update(device,wrapper);
        }
    }
}

