package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface GywlwProjectMapper {
    int deleteByProjectId(String gywlwProjectId);

    int stopByProjectId(String gywlwProjectId);

    int addProject(GywlwProject gywlwProject);

    int insertSelective(GywlwProject record);

    GywlwProject selectByGywlwProjectId(String gywlwProjectId);

    List<GywlwProject> selectByGywlwUserId(@Param("gywlwUserId") String gywlwUserId, @Param("offset") int offset,
                                           @Param("limit") int limit, @Param("projectStatus") Byte projectStatus);

    List<GywlwProject> selectByGywlwUserId1(String gywlwUserId);

    int updateByPrimaryKeySelective(GywlwProject record);

    int updateByProjectId(GywlwProject record);
}