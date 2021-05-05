package com.potato.smart.mapper;

import com.potato.smart.entity.DeviceData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2021-04-20
 */
public interface DeviceDataMapper extends BaseMapper<DeviceData> {
    public Map getDeviceNewData(String sn);
}
