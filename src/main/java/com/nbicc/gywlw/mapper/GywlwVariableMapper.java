package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwVariable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GywlwVariableMapper {
    int deleteByPrimaryKey(String variableId);

    int insert(GywlwVariable record);

    int insertSelective(GywlwVariable record);

    GywlwVariable selectByPrimaryKey(String variableId);

    List<GywlwVariable> selectByProjectId(@Param("projectId") String projectId,
                                          @Param("variableName")String variableName);

    int updateByPrimaryKeySelective(GywlwVariable record);

    int updateByPrimaryKey(GywlwVariable record);
}