package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwVariableRegGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface GywlwVariableRegGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByProjectId(String projectId);

    int insert(GywlwVariableRegGroup record);

    int insertSelective(GywlwVariableRegGroup record);

    List<GywlwVariableRegGroup> selectByProjectId(@Param("projectId")String projectId);

    List<GywlwVariableRegGroup> selectByProjectIdAndVariable(@Param("projectId")String projectId,
                                                             @Param("variableName")String variableName);



    int updateByPrimaryKeySelective(GywlwVariableRegGroup record);

    int updateByPrimaryKey(GywlwVariableRegGroup record);
}