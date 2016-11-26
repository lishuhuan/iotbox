package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface GywlwMessageMapper {
    int deleteByPrimaryKey(Integer messageId);

    int insert(GywlwMessage record);

    int insertSelective(GywlwMessage record);

    List<GywlwMessage> selectByUserId(String userId);

    GywlwMessage selectByUserIdAndMessageType(GywlwMessage msg);

    int updateByPrimaryKeySelective(GywlwMessage record);

    int updateByPrimaryKey(GywlwMessage record);
}