package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwVariable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GywlwVariableMapper {
    int deleteByPrimaryKey(String variableId);

    int deleteByProjectId(String projectId);

    int insert(GywlwVariable record);

    int insertSelective(GywlwVariable record);

    GywlwVariable selectByPrimaryKey(String variableId);

    GywlwVariable selectByProjectIdAndVariableName(@Param("projectId") String projectId,
                                                   @Param("variableName")String variableName);

    List<GywlwVariable> selectByProjectId(@Param("projectId") String projectId,
                                          @Param("variableName")String variableName);
    List<GywlwVariable> selectByProjectIdWithoutTime(String projectId);

    int updateByPrimaryKeySelective(GywlwVariable record);

    int updateByPrimaryKey(GywlwVariable record);
}