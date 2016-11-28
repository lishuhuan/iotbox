package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwUserAdminGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface GywlwUserAdminGroupMapper {
    int deleteByPrimaryKey(String id);

    int insert(GywlwUserAdminGroup record);

    int insertSelective(GywlwUserAdminGroup record);

    int updateByaddIdAndQuitId(@Param("addId") String addId,
                               @Param("quitId")String quitId,
                               @Param("mark")String mark);

    int updateByPrimaryKeySelective(GywlwUserAdminGroup record);

    int updateByPrimaryKey(GywlwUserAdminGroup record);
}