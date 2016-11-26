package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwDevice;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GywlwDeviceMapper {
    int deleteByPrimaryKey(String deviceId);

    int insert(GywlwDevice record);

    int insertSelective(GywlwDevice record);

    GywlwDevice selectByPrimaryKey(String deviceId);

    int updateByPrimaryKeySelective(GywlwDevice record);

    int updateByPrimaryKey(GywlwDevice record);
    
    public List<GywlwDevice> searchDeviceByFactory(@Param("adminId") String adminId);
}