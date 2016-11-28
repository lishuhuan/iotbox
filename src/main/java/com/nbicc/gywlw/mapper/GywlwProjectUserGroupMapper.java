package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwProjectUserGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface GywlwProjectUserGroupMapper {
    int deleteByProjectIdAndUserId(@Param("projectId")String projectId,
                                   @Param("userId")String userId);

    int insert(GywlwProjectUserGroup record);

    int insertSelective(GywlwProjectUserGroup record);

    List<GywlwProjectUserGroup> selectByProjectId(@Param("projectId") String projectId,
                                                  @Param("offset") int offset,
                                                  @Param("limit") int limit);

    GywlwProjectUserGroup selectByProjectIdAndUserId(@Param("projectId") String projectId,
                                                     @Param("userId") String userId);

    List<GywlwProjectUserGroup> selectByUserId(String userId);

    int updateByPrimaryKeySelective(GywlwProjectUserGroup record);

    int updateByPrimaryKey(GywlwProjectUserGroup record);
}