package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwRegInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GywlwRegInfoMapper {
    int deleteByPrimaryKey(String regId);

    int insert(GywlwRegInfo record);

    int insertSelective(GywlwRegInfo record);

    GywlwRegInfo selectByPrimaryKey(String regId);

    GywlwRegInfo selectByRegName(String regName);

    List<GywlwRegInfo> selectByPlcId(String plcId);

    int updateByPrimaryKeySelective(GywlwRegInfo record);

    int updateByPrimaryKey(GywlwRegInfo record);
}