package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwUser;
import com.nbicc.gywlw.Model.GywlwUserAdminGroup;

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
    
    List<GywlwUser> getFactoryLimitUser(@Param("name") String name,@Param("id") String id);
    
    void addFactoryLimitUserDistribution(GywlwUserAdminGroup gywlwUserAdminGroup);
    
    void deleteFactoryLimitUserDistribution(@Param("factoryId") String factoryId,@Param("userId") String userId);
    
    

}