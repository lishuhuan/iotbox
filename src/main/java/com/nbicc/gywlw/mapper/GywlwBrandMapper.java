package com.nbicc.gywlw.mapper;

import com.nbicc.gywlw.Model.GywlwBrand;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GywlwBrandMapper {
    int deleteByPrimaryKey(String id);

    int insert(GywlwBrand record);

    int insertSelective(GywlwBrand record);

    GywlwBrand selectByPrimaryKey(String id);

    GywlwBrand selectByProductModel(String model);

    int updateByPrimaryKeySelective(GywlwBrand record);

    int updateByPrimaryKey(GywlwBrand record);
}