package com.potato.smart.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.potato.smart.entity.Device;
import com.potato.smart.entity.UserDevice;
import com.potato.smart.mqtt.Mqtt;
import com.potato.smart.service.DeviceDataService;
import com.potato.smart.service.DeviceService;
import com.potato.smart.service.UserDeviceService;
import com.potato.smart.utils.JwtUtils;
import com.potato.smart.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author zhoubin
 * @since 2021-04-20
 */
@RestController
@RequestMapping("/smart/device")
@CrossOrigin
public class DeviceController {
    @Autowired
    private DeviceDataService deviceDataService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private UserDeviceService userDeviceService;

    @PostMapping("/deleteDevice")
    public R deleteDevice(@RequestBody List<String> list){
        if(list.size()==0){
            return R.error().message("不能传递空数据");
        }
        boolean b = userDeviceService.removeByIds(list);
        return b?R.ok().message("删除成功"):R.error().message("删除失败");
    }

    // 发送信息
    @PostMapping("/send")
    public void test2(@RequestBody Map<String,String> map) {
        Mqtt mqtt = new Mqtt();
        mqtt.publish(map.get("topic"),map.get("data"));
    }

    @PostMapping("/addDevice")
    public R addDevice(@RequestBody Device device, HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("sn",device.getSn());
        Device one = deviceService.getOne(wrapper);
        if(one==null){
            return R.error().message("设备不存在");
        }
        QueryWrapper userDeviceWrapper = new QueryWrapper();
        userDeviceWrapper.eq("did", one.getId());
        UserDevice userdevice = userDeviceService.getOne(userDeviceWrapper);
        if(userdevice==null){
            UserDevice ud = new UserDevice();
            ud.setUid(Integer.valueOf(userId));
            ud.setDid(one.getId());
            System.out.println(ud);
           userDeviceService.save(ud);
        }else{
            return R.error().message("设备已被绑定");
        }
        return R.ok().message("添加成功");
    }
}

