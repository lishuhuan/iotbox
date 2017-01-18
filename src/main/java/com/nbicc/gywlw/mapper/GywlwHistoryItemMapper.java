package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwHistoryItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
@Mapper
public interface GywlwHistoryItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(GywlwHistoryItem record);

    int insertSelective(GywlwHistoryItem record);

    int insertBatch(List<GywlwHistoryItem> list);
    int insertBatch1(List<GywlwHistoryItem> list);

    List<GywlwHistoryItem> selectByVariableName(@Param("variableName") String variableName,
                                                @Param("projectId")String projectId);

    List<GywlwHistoryItem> selectwarning(@Param("projectId")String projectId,
                                         @Param("variableName") String variableName,
                                         @Param("startTime")String startTime,
                                         @Param("endTime")String endTime);

    int updateByPrimaryKeySelective(GywlwHistoryItem record);

    int updateByPrimaryKey(GywlwHistoryItem record);
    
    List<GywlwHistoryItem> getHistoryData(@Param("deviceId") String deviceId);
    
    GywlwHistoryItem getAlarmDetail(@Param("itemId") String itemId);
    
    List<GywlwHistoryItem> getDeviceAlarmlist(@Param("startTime") String startTime,
                                                     @Param("endTime") String endTime,
                                                     @Param("deviceId") String deviceId);

    List<GywlwHistoryItem> getDataForTrend(@Param("regId")String regId,
                                                  @Param("startTime") Date startTime,
                                                  @Param("endTime") Date endTime);

    List<GywlwHistoryItem> getDataForReg(@Param("regId")String regId,
                                           @Param("startTime") Date startTime,
                                           @Param("endTime") Date endTime);

    GywlwHistoryItem getLastTimeByPlcId(String plcId);


}