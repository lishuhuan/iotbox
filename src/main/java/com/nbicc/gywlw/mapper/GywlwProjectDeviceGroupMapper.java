package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwProjectDeviceGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GywlwProjectDeviceGroupMapper {
    int deleteByPrimaryKey(String id);

    int deleteByProjectId(String projectId);

    int insert(GywlwProjectDeviceGroup record);

    int insertSelective(GywlwProjectDeviceGroup record);

    GywlwProjectDeviceGroup selectByPrimaryKey(String id);

    GywlwProjectDeviceGroup selectByDeviceId(String deviceId);

    List<GywlwProjectDeviceGroup> selectByProjectId(String projectId);

    GywlwProjectDeviceGroup selectByDeviceIdAndProjectId(@Param("deviceId") String deviceId,
                                                         @Param("projectId") String projectId);

    int updateByPrimaryKeySelective(GywlwProjectDeviceGroup record);

    int updateByPrimaryKey(GywlwProjectDeviceGroup record);
}