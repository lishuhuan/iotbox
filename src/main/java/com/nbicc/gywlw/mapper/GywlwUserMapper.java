package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwUser;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GywlwUserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(GywlwUser record);

    int insertSelective(GywlwUser record);

    GywlwUser selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(GywlwUser record);

    int updateByPrimaryKey(GywlwUser record);

    GywlwUser selectByPhone(String phone);

    GywlwUser selectByPhoneWithPsd(String phone);

    List<GywlwUser> searchUserByFactoy(@Param("factoryId") String factoryId,@Param("level") int level,@Param("name") String name);
    

}