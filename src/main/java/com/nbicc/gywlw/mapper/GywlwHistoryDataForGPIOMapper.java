package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwHistoryDataForGPIO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GywlwHistoryDataForGPIOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GywlwHistoryDataForGPIO record);

    int insertBatch(List<GywlwHistoryDataForGPIO> list);

    int insertSelective(GywlwHistoryDataForGPIO record);

    GywlwHistoryDataForGPIO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GywlwHistoryDataForGPIO record);

    int updateByPrimaryKey(GywlwHistoryDataForGPIO record);
}