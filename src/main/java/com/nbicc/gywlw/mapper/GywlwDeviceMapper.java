package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwDevice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GywlwDeviceMapper {
    int deleteByPrimaryKey(String deviceId);

    int insert(GywlwDevice record);

    int insertSelective(GywlwDevice record);

    GywlwDevice selectByPrimaryKey(String deviceId);

    int updateByPrimaryKeySelective(GywlwDevice record);

    int updateByPrimaryKey(GywlwDevice record);
}