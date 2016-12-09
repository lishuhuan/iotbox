package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwUser;
import com.nbicc.gywlw.Model.GywlwUserAdminGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GywlwUserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(GywlwUser record);

    int insertSelective(GywlwUser record);

    GywlwUser selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(GywlwUser record);

    int updateByPrimaryKey(GywlwUser record);

    //不包含密码，通常使用
    GywlwUser selectByPhone(String phone);

    //包含密码敏感信息，仅作验证密码时可用
    GywlwUser selectByPhoneWithPsd(String phone);

    List<GywlwUser> searchUserByFactoy(@Param("factoryId") String factoryId,@Param("level") int level,@Param("name") String name);
    
    List<GywlwUser> getFactoryLimitUser(@Param("name") String name,@Param("id") String id);
    
    void addFactoryLimitUserDistribution(GywlwUserAdminGroup gywlwUserAdminGroup);
    
    void deleteFactoryLimitUserDistribution(@Param("factoryId") String factoryId,@Param("userId") String userId);
    
    

}