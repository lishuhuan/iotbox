package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwDataTrend;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface GywlwDataTrendMapper {
    int deleteByPrimaryKey(Integer trendId);

    int insert(GywlwDataTrend record);

    int insertSelective(GywlwDataTrend record);

    GywlwDataTrend selectByPrimaryKey(Integer trendId);

    List<GywlwDataTrend> selectByPhotoName(String photoName);

    int updateByPrimaryKeySelective(GywlwDataTrend record);

    int updateByPrimaryKey(GywlwDataTrend record);
}