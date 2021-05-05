package com.potato.smart.mapper;

import com.potato.smart.entity.UserDevice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2021-04-20
 */
public interface UserDeviceMapper extends BaseMapper<UserDevice> {
    public List<Map<String,String>> getUserDeviceList(String id);
}
