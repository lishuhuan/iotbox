package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwDeviceGpio;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GywlwDeviceGpioMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByDeviceId(String deviceId);

    int insert(GywlwDeviceGpio record);

    int insertSelective(GywlwDeviceGpio record);

    GywlwDeviceGpio selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GywlwDeviceGpio record);

    int updateByPrimaryKey(GywlwDeviceGpio record);
}