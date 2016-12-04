package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwWarningRules;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface GywlwWarningRulesMapper {
    int deleteByPrimaryKey(Integer ruleId);

    int insert(GywlwWarningRules record);

    int insertSelective(GywlwWarningRules record);

    GywlwWarningRules selectByPrimaryKey(Integer ruleId);

    List<GywlwWarningRules> selectByDeviceId(String deviceId);

    int updateByPrimaryKeySelective(GywlwWarningRules record);

    int updateByPrimaryKey(GywlwWarningRules record);
}