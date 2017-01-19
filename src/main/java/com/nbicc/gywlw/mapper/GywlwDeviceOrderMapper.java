package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwDeviceOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GywlwDeviceOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GywlwDeviceOrder record);

    int insertSelective(GywlwDeviceOrder record);

    GywlwDeviceOrder selectByPrimaryKey(Integer id);

    GywlwDeviceOrder selectByDeviceId(String deviceId);

    int updateByPrimaryKeySelective(GywlwDeviceOrder record);

    int updateByPrimaryKey(GywlwDeviceOrder record);
}