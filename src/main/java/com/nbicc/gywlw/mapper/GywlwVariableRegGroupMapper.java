package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwVariableRegGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface GywlwVariableRegGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GywlwVariableRegGroup record);

    int insertSelective(GywlwVariableRegGroup record);

    List<GywlwVariableRegGroup> selectByProjectIdAndVariableId(@Param("projectId")String projectId);

    int updateByPrimaryKeySelective(GywlwVariableRegGroup record);

    int updateByPrimaryKey(GywlwVariableRegGroup record);
}