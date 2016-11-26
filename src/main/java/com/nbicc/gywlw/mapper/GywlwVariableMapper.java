package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwVariable;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GywlwVariableMapper {
    int deleteByPrimaryKey(String variableId);

    int insert(GywlwVariable record);

    int insertSelective(GywlwVariable record);

    GywlwVariable selectByPrimaryKey(String variableId);

    List<GywlwVariable> selectByProjectId(String projectId);

    int updateByPrimaryKeySelective(GywlwVariable record);

    int updateByPrimaryKey(GywlwVariable record);
}