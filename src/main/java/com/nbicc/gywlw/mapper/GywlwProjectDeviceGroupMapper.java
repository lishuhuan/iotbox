package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwProjectDeviceGroup;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GywlwProjectDeviceGroupMapper {
    int deleteByPrimaryKey(String id);

    int deleteByProjectId(String projectId);

    int insert(GywlwProjectDeviceGroup record);

    int insertSelective(GywlwProjectDeviceGroup record);

    GywlwProjectDeviceGroup selectByPrimaryKey(String id);

    GywlwProjectDeviceGroup selectByDeviceId(String deviceId);

    int updateByPrimaryKeySelective(GywlwProjectDeviceGroup record);

    int updateByPrimaryKey(GywlwProjectDeviceGroup record);
}