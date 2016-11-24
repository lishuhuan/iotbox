package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwRegInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GywlwRegInfoMapper {
    int deleteByPrimaryKey(String regId);

    int insert(GywlwRegInfo record);

    int insertSelective(GywlwRegInfo record);

    GywlwRegInfo selectByPrimaryKey(String regId);

    int updateByPrimaryKeySelective(GywlwRegInfo record);

    int updateByPrimaryKey(GywlwRegInfo record);
}