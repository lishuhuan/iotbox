package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwPlcInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface GywlwPlcInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(GywlwPlcInfo record);

    int insertSelective(GywlwPlcInfo record);

    GywlwPlcInfo selectByPrimaryKey(String id);

    GywlwPlcInfo selectBySubDeviceId(String id);

    List<GywlwPlcInfo> selectByDeviceId(String deviceId);

    GywlwPlcInfo selectByDeviceId1(String deviceId);

    int updateByPrimaryKeySelective(GywlwPlcInfo record);

    int updateByPrimaryKey(GywlwPlcInfo record);
}