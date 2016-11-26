package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwHistoryItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface GywlwHistoryItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(GywlwHistoryItem record);

    int insertSelective(GywlwHistoryItem record);

    List<GywlwHistoryItem> selectByVariableName(@Param("variableName") String variableName,
                                                @Param("projectId")String projectId);

    List<GywlwHistoryItem> selectwarning(@Param("projectId")String projectId,
                                         @Param("variableName") String variableName,
                                         @Param("startTime")String startTime,
                                         @Param("endTime")String endTime);

    int updateByPrimaryKeySelective(GywlwHistoryItem record);

    int updateByPrimaryKey(GywlwHistoryItem record);
}