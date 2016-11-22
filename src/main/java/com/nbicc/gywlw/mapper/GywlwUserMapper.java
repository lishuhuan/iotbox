package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GywlwUserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(GywlwUser record);

    int insertSelective(GywlwUser record);

    GywlwUser selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(GywlwUser record);

    int updateByPrimaryKey(GywlwUser record);

    GywlwUser selectByPhone(String phone);


}