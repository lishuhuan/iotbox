package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwVariable;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GywlwVariableMapper {
    int deleteByPrimaryKey(String variableId);

    int insert(GywlwVariable record);

    int insertSelective(GywlwVariable record);

    GywlwVariable selectByPrimaryKey(String variableId);

    int updateByPrimaryKeySelective(GywlwVariable record);

    int updateByPrimaryKey(GywlwVariable record);
}