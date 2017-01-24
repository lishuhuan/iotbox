package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwDeviceGpio;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GywlwDeviceGpioMapper {
    int deleteByPrimaryKey(String id);

    int deleteByDeviceId(String deviceId);

    int insert(GywlwDeviceGpio record);

    int insertSelective(GywlwDeviceGpio record);

    GywlwDeviceGpio selectByPrimaryKey(String id);

    GywlwDeviceGpio selectByDeviceIdAndGpioId(@Param("deviceId")String deviceId,
                                              @Param("gpioId")String gpioId);


    List<GywlwDeviceGpio> selectByDeviceId(String deviceId);

    int updateByPrimaryKeySelective(GywlwDeviceGpio record);

    int updateByPrimaryKey(GywlwDeviceGpio record);
}