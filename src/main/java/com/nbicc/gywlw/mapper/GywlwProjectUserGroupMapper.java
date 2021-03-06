package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwProject;
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

    List<GywlwProjectUserGroup> selectByProjectId(@Param("projectId") String projectId);

    List<GywlwProject> selectProjectByUserId(@Param("userId") String userId,
                                             @Param("projectStatus")Byte projectStatus);

    GywlwProjectUserGroup selectByProjectIdAndUserId(@Param("projectId") String projectId,
                                                     @Param("userId") String userId);

    List<GywlwProjectUserGroup> selectByUserId(String userId);

    int updateByPrimaryKeySelective(GywlwProjectUserGroup record);

    int updateByPrimaryKey(GywlwProjectUserGroup record);

    int deleteByaddId(@Param("addId")String addId);
}