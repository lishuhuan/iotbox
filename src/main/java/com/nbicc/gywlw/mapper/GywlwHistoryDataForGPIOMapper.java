package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwHistoryDataForGPIO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GywlwHistoryDataForGPIOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GywlwHistoryDataForGPIO record);

    int insertBatch(List<GywlwHistoryDataForGPIO> list);

    int insertSelective(GywlwHistoryDataForGPIO record);

    GywlwHistoryDataForGPIO selectByPrimaryKey(Integer id);

    List<GywlwHistoryDataForGPIO> getHistoryData(@Param("deviceId")String deviceId,
                                                 @Param("gpioId")String gpioId);

    List<GywlwHistoryDataForGPIO> getLatestDataByVariableName(@Param("projectId")String projectId,
                                                              @Param("variableName")String variableName);

    List<GywlwHistoryDataForGPIO> getLatestData(String deviceId);

    GywlwHistoryDataForGPIO getLastTimeByDeviceId(String deviceId);

    int updateByPrimaryKeySelective(GywlwHistoryDataForGPIO record);

    int updateByPrimaryKey(GywlwHistoryDataForGPIO record);
}