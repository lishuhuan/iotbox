package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GywlwDeviceMapper {
    int deleteByPrimaryKey(String deviceId);

    int insert(GywlwDevice record);

    int insertSelective(GywlwDevice record);

    List<GywlwDevice> selectByUserIdAndDeviceSn(@Param("userId")String userId,
                                               @Param("deviceSn")String deviceSn);
    List<GywlwDevice> selectByUserIdAndDeviceSnWithAdmin(@Param("userId")String userId,
                                                   @Param("deviceSn")String deviceSn);

    GywlwDevice selectByDeviceId(String deviceId);

    int updateByPrimaryKeySelective(GywlwDevice gywlwDevice);

    int updateByPrimaryKey(GywlwDevice record);
}