package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwWarningRules;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GywlwWarningRulesMapper {
    int deleteByPrimaryKey(Integer ruleId);

    int deleteByPlcId(String plcId);

    int insert(GywlwWarningRules record);

    int insertSelective(GywlwWarningRules record);

    GywlwWarningRules selectByPrimaryKey(Integer ruleId);

    int updateByPrimaryKeySelective(GywlwWarningRules record);

    int updateByPrimaryKey(GywlwWarningRules record);

    List<GywlwWarningRules> selectByDeviceId(String deviceId);
}